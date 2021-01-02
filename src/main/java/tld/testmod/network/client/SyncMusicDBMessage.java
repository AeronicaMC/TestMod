package tld.testmod.network.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.common.storage.capability.IMusicDB;
import tld.testmod.common.storage.capability.MusicDBCapability;
import tld.testmod.common.storage.capability.SyncType;
import tld.testmod.network.AbstractMessage;

import java.io.IOException;

public class SyncMusicDBMessage extends AbstractMessage.AbstractClientMessage<SyncMusicDBMessage>
{
    @CapabilityInject(IMusicDB.class)
    private static final Capability<IMusicDB> MUSIC_DB_CAP = MusicDBCapability.nonNullInjected();
    private SyncType syncType;
    private NBTTagCompound data;
    private boolean session;

    public SyncMusicDBMessage() {/* Required by the PacketDispatcher */}

    public SyncMusicDBMessage(IMusicDB musicDB, SyncType syncType)
    {
        this.syncType = syncType;
        switch (syncType)
        {
            case ALL:
                this.data = new NBTTagCompound();
                this.data = (NBTTagCompound) MUSIC_DB_CAP.writeNBT(musicDB, null);
                break;

            case SESSION:
                this.session = musicDB.isSessionOpen();
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
            case ALL:
                this.data = buffer.readCompoundTag();
                break;
            case SESSION:
                session = buffer.readBoolean();
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
            case ALL:
                buffer.writeCompoundTag(this.data);
                break;
            case SESSION:
                buffer.writeBoolean(session);
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
                    case ALL:
                        MUSIC_DB_CAP.readNBT(musicDB, null, data);
                        break;
                    case SESSION:
                        if (session)
                            musicDB.openSession();
                        else
                            musicDB.closeSession();
                        break;

                    default:
                }
        }
    }
}
