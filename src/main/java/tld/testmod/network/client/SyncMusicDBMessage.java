package tld.testmod.network.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import de.javakaffee.kryoserializers.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.common.storage.capability.IMusicDB;
import tld.testmod.common.storage.capability.MusicDBCapability;
import tld.testmod.common.storage.capability.SyncType;
import tld.testmod.common.storage.models.PlayList;
import tld.testmod.common.storage.models.Song;
import tld.testmod.common.storage.models.Tag;
import tld.testmod.common.storage.models.User;
import tld.testmod.network.AbstractMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class SyncMusicDBMessage extends AbstractMessage.AbstractClientMessage<SyncMusicDBMessage>
{
    @CapabilityInject(IMusicDB.class)
    private static final Capability<IMusicDB> MUSIC_DB_CAP = MusicDBCapability.nonNullInjected();
    private SyncType syncType;
    private NBTTagCompound data;
    private boolean session;
    private byte[] byteBuffer;
    private Kryo kryo;

    private List<PlayList> playLists;
    private List<Song> songs;
    private List<Tag> tags;
    private List<User> users;

    public SyncMusicDBMessage() {
        /* Required by the PacketDispatcher */
        initKryo();
    }

    public SyncMusicDBMessage(IMusicDB musicDB, SyncType syncType)
    {
        initKryo();
        this.syncType = syncType;
        switch (syncType)
        {
            case ALL_NBT:
                this.data = new NBTTagCompound();
                this.data = (NBTTagCompound) MUSIC_DB_CAP.writeNBT(musicDB, null);
                break;
            case SESSION_STATE:
                this.session = musicDB.isSessionOpen();
                break;
            case PLAY_LISTS:
                playLists = Arrays.asList(musicDB.getPlaylists());
                break;
            case SONGS:
                songs = Arrays.asList(musicDB.getSongs());
                break;
            case TAGS:
                tags = Arrays.asList(musicDB.getTags());
                break;
            case USERS:
                users = Arrays.asList(musicDB.getUsers());
                break;
            default:
        }
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        syncType = buffer.readEnumValue(SyncType.class);
        switch (syncType)
        {
            case ALL_NBT:
                this.data = buffer.readCompoundTag();
                break;
            case SESSION_STATE:
                session = buffer.readBoolean();
                break;
            case PLAY_LISTS:
                playLists = readList(buffer, Arrays.asList( new PlayList[1] ).getClass());
                break;
            case SONGS:
                songs = readList(buffer, Arrays.asList( new Song[1] ).getClass());
                break;
            case TAGS:
                tags = readList(buffer, Arrays.asList( new Tag[1] ).getClass());
                break;
            case USERS:
                users = readList(buffer, Arrays.asList( new User[1] ).getClass());
                break;

            default:
        }
    }

    @Override
    protected void write(PacketBuffer buffer)
    {
        buffer.writeEnumValue(syncType);
        switch (syncType)
        {
            case ALL_NBT:
                buffer.writeCompoundTag(this.data);
                break;
            case SESSION_STATE:
                buffer.writeBoolean(session);
                break;
            case PLAY_LISTS:
                writeList(buffer, playLists);
                break;
            case SONGS:
                writeList(buffer, songs);
                break;
            case TAGS:
                writeList(buffer, tags);
                break;
            case USERS:
                writeList(buffer, users);
                break;

            default:
        }
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        if (player.hasCapability(MUSIC_DB_CAP, null))
        {
            final IMusicDB musicDB = player.getCapability(MUSIC_DB_CAP, null);
            if (musicDB != null)
                switch (syncType)
                {
                    case ALL_NBT:
                        MUSIC_DB_CAP.readNBT(musicDB, null, data);
                        break;
                    case SESSION_STATE:
                        if (session)
                            musicDB.openSession();
                        else
                            musicDB.closeSession();
                        break;
                    case PLAY_LISTS:
                        musicDB.setPlaylists((PlayList[]) playLists.toArray());
                        break;
                    case SONGS:
                        musicDB.setSongs((Song[]) songs.toArray());
                        break;
                    case TAGS:
                        musicDB.setTags((Tag[]) tags.toArray());
                        break;
                    case USERS:
                        musicDB.setUsers((User[]) users.toArray());
                        break;

                    default:
                }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> readList(PacketBuffer buffer, Class<?> clazz)
    {
        List<T> list;
        // Deserialize data object from a byte array
        byteBuffer = buffer.readByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer) ;
        Input input = new Input(bis);
        list = (List<T>) kryo.readObject(input, clazz);
        input.close();
        return list;
    }

    @SuppressWarnings("all")
    private <T> void writeList(PacketBuffer buffer, List<T> list)
    {
        // Serialize data object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        Output output = new Output(bos) ;
        kryo.writeObject(output, (List<T>)list);

        // Get the bytes of the serialized object
        byteBuffer = output.toBytes();
        output.close();
        buffer.writeByteArray(byteBuffer);
    }

    private void initKryo()
    {
        Log.DEBUG();
        kryo = new Kryo();
        kryo.register(PlayList.class);
        kryo.register(Song.class);
        kryo.register(Tag.class);
        kryo.register(User.class);
        kryo.register( Arrays.asList( new PlayList[1] ).getClass(), new ArraysAsListSerializer());
        kryo.register( Arrays.asList( new Song[1] ).getClass(), new ArraysAsListSerializer());
        kryo.register( Arrays.asList( new Tag[1] ).getClass(), new ArraysAsListSerializer());
        kryo.register( Arrays.asList( new User[1] ).getClass(), new ArraysAsListSerializer());
        kryo.register( Collections.EMPTY_LIST.getClass(), new CollectionsEmptyListSerializer());
        kryo.register( Collections.EMPTY_MAP.getClass(), new CollectionsEmptyMapSerializer());
        kryo.register( Collections.EMPTY_SET.getClass(), new CollectionsEmptySetSerializer());
        kryo.register( Collections.singletonList( "" ).getClass(), new CollectionsSingletonListSerializer() );
        kryo.register(Collections.singleton( new HashSet<Long>()).getClass(), new CollectionsSingletonSetSerializer());
        kryo.register( Collections.singletonMap( "", "" ).getClass(), new CollectionsSingletonMapSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers( kryo );
        SynchronizedCollectionsSerializer.registerSerializers( kryo );
    }
}
