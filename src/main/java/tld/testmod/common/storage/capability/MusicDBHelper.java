package tld.testmod.common.storage.capability;

import com.iciql.Db;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tld.testmod.common.storage.dao.ModelDao;
import tld.testmod.common.storage.models.PlayList;
import tld.testmod.common.storage.models.PlayListEntry;
import tld.testmod.common.storage.models.Song;
import tld.testmod.common.storage.models.Tag;
import tld.testmod.common.utils.ModRuntimeException;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.client.SyncMusicDBMessage;

import javax.annotation.Nullable;
import java.sql.SQLException;
import java.util.*;

import static tld.testmod.common.storage.ServerDataManager.getConnection;

@SuppressWarnings("ConstantConditions")
public class MusicDBHelper
{
    @CapabilityInject(IMusicDB.class)
    private static final Capability<IMusicDB> MUSIC_DB_CAP = MusicDBCapability.nonNullInjected();

    private MusicDBHelper() { /* NOP */}

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
            musicDB.resetDbData();
            List<PlayListEntry> playListEntries = new ArrayList<>();
            UUID uid = player.getPersistentID();
            try (Db db = Db.open(getConnection()))
            {
                ModelDao dao = db.open(ModelDao.class);
                // Query playlists and their entries
                musicDB.setPlaylists(dao.getPlayLists(uid));
                for (PlayList playList : musicDB.getPlaylists())
                {
                    playListEntries.addAll(Arrays.asList(dao.getPlayListEntries(playList.pid)));
                }
                musicDB.setPlayListEntries(playListEntries.toArray(new PlayListEntry[0]));
                playListEntries.clear();

                musicDB.setSongs(dao.getSongs(uid));
                musicDB.setTags(dao.getTags(uid));

                if (isPlayerOp(player) || player.isCreative())
                    musicDB.setUsers(dao.getAllUsers()); // In MP ONLY or Creative OP'd players get AllUsers
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
            sync(player, SyncType.PLAY_LIST_ENTRIES);
            sync(player, SyncType.SONGS);
            sync(player, SyncType.TAGS);
            sync(player, SyncType.USERS);
        }
    }

    /**
     * Useful only on server MP. Will always be false if called client side or in SP.
     * @param player in question
     * @return true if Op's
     */
    private static boolean isPlayerOp(EntityPlayer player)
    {
        return  (!player.world.isRemote) && (FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null);
    }
    public static void syncAll(EntityPlayer player)
    {
        sync(player, SyncType.ALL_NBT);
    }

    public static void sync(@Nullable EntityPlayer player, SyncType syncType)
    {
        PacketDispatcher.sendTo(new SyncMusicDBMessage(player.getCapability(MUSIC_DB_CAP, null), syncType), (EntityPlayerMP)player);
    }
}
