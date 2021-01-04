package tld.testmod.network.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.common.storage.capability.MusicDBHelper;
import tld.testmod.common.storage.capability.RequestType;
import tld.testmod.common.storage.capability.SyncType;
import tld.testmod.network.AbstractMessage;

public class MusicDBServerRequest extends AbstractMessage.AbstractServerMessage<MusicDBServerRequest>
{
    private RequestType requestType;

    public MusicDBServerRequest() {/* Required by the PacketDispatcher */}

    public MusicDBServerRequest(RequestType requestType)
    {
        this.requestType =  requestType;
    }

    @Override
    protected void read(PacketBuffer buffer)
    {
        requestType = buffer.readEnumValue(RequestType.class);
    }

    @Override
    protected void write(PacketBuffer buffer)
    {
        buffer.writeEnumValue(requestType);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
            if (MusicDBHelper.isSessionOpen(player))
                MusicDBHelper.closeSession(player);
            else
                MusicDBHelper.openSession(player);
            MusicDBHelper.sync(player, SyncType.SESSION_STATE);
    }
}
