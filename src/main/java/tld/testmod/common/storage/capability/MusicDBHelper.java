package tld.testmod.common.storage.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import tld.testmod.common.utils.ModRuntimeException;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.client.SyncMusicDBMessage;

import javax.annotation.Nullable;
import java.util.Objects;

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
    private static IMusicDB getImpl(EntityPlayer player)
    {
        IMusicDB musicDB;
        if (player.hasCapability(Objects.requireNonNull(MUSIC_DB_CAP), null))
            musicDB = player.getCapability(MUSIC_DB_CAP, null);
        else
            throw new ModRuntimeException("IMusicDB capability is null");
        return musicDB;
    }

    public static void syncAll(EntityPlayer player)
    {
        sync(player, SyncType.ALL);
    }

    public static void sync(EntityPlayer player, SyncType syncType)
    {
        PacketDispatcher.sendTo(new SyncMusicDBMessage(player.getCapability(MUSIC_DB_CAP, null), syncType), (EntityPlayerMP)player);
    }
}
