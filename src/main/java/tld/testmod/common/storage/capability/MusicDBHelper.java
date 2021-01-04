package tld.testmod.common.storage.capability;

import com.iciql.Db;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import tld.testmod.common.storage.dao.ModelDao;
import tld.testmod.common.storage.models.PlayList;
import tld.testmod.common.storage.models.Song;
import tld.testmod.common.storage.models.Tag;
import tld.testmod.common.utils.ModRuntimeException;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.client.SyncMusicDBMessage;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

import static tld.testmod.common.storage.ServerDataManager.getConnection;

@SuppressWarnings("ConstantConditions")
public class MusicDBHelper
{
    @CapabilityInject(IMusicDB.class)
    private static final Capability<IMusicDB> MUSIC_DB_CAP = MusicDBCapability.nonNullInjected();

    private MusicDBHelper() { /** NOP **/}

    public static boolean isSessionOpen(@Nullable EntityPlayer player)
    {
        return (player != null) && (getImpl(player) != null) && getImpl(player).isSessionOpen();
    }

    public static void openSession(EntityPlayer player)
    {
        if (player != null) getImpl(player).openSession();
    }

    public static void closeSession(EntityPlayer player)
    {
        if (player != null) getImpl(player).closeSession();
    }

    @Nullable
    public static IMusicDB getImpl(EntityPlayer player)
    {
        IMusicDB musicDB;
        if (player.hasCapability(Objects.requireNonNull(MUSIC_DB_CAP), null))
            musicDB = player.getCapability(MUSIC_DB_CAP, null);
        else
            throw new ModRuntimeException("IMusicDB capability is null");
        return musicDB;
    }

    /**
     * Server Side
     * Request Users Music Data
     */
    public static void collectUserMusicData(EntityPlayer player, boolean sync)
    {
        IMusicDB musicDB = getImpl(player);
        if (musicDB != null && !player.world.isRemote)
        {
            UUID uid = player.getPersistentID();
            try (Db db = Db.open(getConnection()))
            {
                ModelDao dao = db.open(ModelDao.class);
                musicDB.setPlaylists(dao.getPlayLists(uid));
                musicDB.setSongs(dao.getSongs(uid));
                musicDB.setTags(dao.getTags(uid));
                musicDB.setUsers(dao.getAllUsers());
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
            musicDB.getPlayListIDs().clear();
            musicDB.getSongIDs().clear();
            musicDB.getTagTDs().clear();
            for (PlayList playList : musicDB.getPlaylists())
                musicDB.getPlayListIDs().add(playList.pid);
            for (Song song : musicDB.getSongs())
                musicDB.getSongIDs().add(song.sid);
            for (Tag tag : musicDB.getTags())
                musicDB.getTagTDs().add(tag.tid);
        }
        if (sync)
        {
            sync(player, SyncType.PLAY_LISTS);
            sync(player, SyncType.SONGS);
            sync(player, SyncType.TAGS);
            sync(player, SyncType.USERS);
        }
    }

    public static void syncAll(EntityPlayer player)
    {
        sync(player, SyncType.ALL_NBT);
    }

    public static void sync(@Nullable EntityPlayer player, SyncType syncType)
    {
        if ((player != null) && !player.world.isRemote)
            PacketDispatcher.sendTo(new SyncMusicDBMessage(player.getCapability(MUSIC_DB_CAP, null), syncType), (EntityPlayerMP)player);
    }
}
