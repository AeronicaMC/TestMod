package tld.testmod.network.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.common.storage.capability.IMusicDB;
import tld.testmod.common.storage.capability.MusicDBCapability;
import tld.testmod.common.storage.capability.SyncType;
import tld.testmod.common.storage.models.*;
import tld.testmod.network.AbstractMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class SyncMusicDBMessage extends AbstractMessage.AbstractClientMessage<SyncMusicDBMessage>
{
    @CapabilityInject(IMusicDB.class)
    private static final Capability<IMusicDB> MUSIC_DB_CAP = MusicDBCapability.nonNullInjected();
    private SyncType syncType;
    private NBTTagCompound data;
    private boolean session;
    private byte[] byteBuffer;
    private final Kryo kryo = kryoThreadPool.get();

    // What's better Object[] array or List<Object> ?? Both are possible
    private PlayList[] playLists;
    private PlayListEntry[] playListEntries;
    private Song[] songs;
    private Tag[] tags;
    // private List<User> users;
    private User[] users;

    public SyncMusicDBMessage() { /* Required by the PacketDispatcher */ }

    public SyncMusicDBMessage(IMusicDB musicDB, SyncType syncType)
    {
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
                playLists = musicDB.getPlaylists();
                break;
            case PLAY_LIST_ENTRIES:
                playListEntries = musicDB.getPlayListEntries();
                break;
            case SONGS:
                songs = musicDB.getSongs();
                break;
            case TAGS:
                tags = musicDB.getTags();
                break;
            case USERS:
                //users = Arrays.asList(musicDB.getUsers());
                users = musicDB.getUsers();
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
                playLists = readArray(buffer, PlayList[].class);
                break;
            case PLAY_LIST_ENTRIES:
                playListEntries = readArray(buffer, PlayListEntry[].class);
                break;
            case SONGS:
                songs = readArray(buffer, Song[].class);
                break;
            case TAGS:
                tags = readArray(buffer, Tag[].class);
                break;
            case USERS:
                //users = readList(buffer, Arrays.asList( new User[1] ).getClass());
                users = readArray(buffer, User[].class);
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
                writeArray(buffer, playLists);
                break;
            case PLAY_LIST_ENTRIES:
                writeArray(buffer, playListEntries);
                break;
            case SONGS:
                writeArray(buffer, songs);
                break;
            case TAGS:
                writeArray(buffer, tags);
                break;
            case USERS:
                writeArray(buffer, users);
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
                        musicDB.setPlaylists(playLists);
                        break;
                    case PLAY_LIST_ENTRIES:
                        musicDB.setPlayListEntries(playListEntries);
                        break;
                    case SONGS:
                        musicDB.setSongs(songs);
                        break;
                    case TAGS:
                        musicDB.setTags(tags);
                        break;
                    case USERS:
                        musicDB.setUsers(users);
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

    private <T> void writeList(PacketBuffer buffer, List<T> list)
    {
        // Serialize data object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        Output output = new Output(bos) ;
        kryo.writeObject(output, list);

        // Get the bytes of the serialized object
        byteBuffer = output.toBytes();
        output.close();
        buffer.writeByteArray(byteBuffer);
    }

    // At the moment I prefer the Object[] array generic methods. Their use is cleaner.
    private <T> T[] readArray(PacketBuffer buffer, Class<T[]> clazz)
    {
        T[] array;
        // Deserialize data object from a byte array
        byteBuffer = buffer.readByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(byteBuffer) ;
        Input input = new Input(bis);
        array = kryo.readObject(input, clazz);
        input.close();
        return array;
    }

    private <T> void writeArray(PacketBuffer buffer, T[] array)
    {
        // Serialize data object to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        Output output = new Output(bos) ;
        kryo.writeObject(output, array);

        // Get the bytes of the serialized object
        byteBuffer = output.toBytes();
        output.close();
        buffer.writeByteArray(byteBuffer);
    }

    static private final ThreadLocal<Kryo> kryoThreadPool = ThreadLocal.withInitial(() ->
        {
            // TODO: Remove Log.DEBUG or make Log.WARN for production use
            Log.DEBUG();
            Kryo kryo = new Kryo();
            kryo.register(PlayList.class);
            kryo.register(PlayListEntry.class);
            kryo.register(Song.class);
            kryo.register(Tag.class);
            kryo.register(User.class);
            kryo.register(PlayList[].class);
            kryo.register(PlayListEntry[].class);
            kryo.register(Song[].class);
            kryo.register(Tag[].class);
            kryo.register(User[].class);
            kryo.register(Object[].class);
            return kryo;
        });
}
