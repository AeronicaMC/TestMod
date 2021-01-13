package tld.testmod.network.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.Main;
import tld.testmod.network.AbstractMessage;

import java.io.IOException;

public class OpenGuiMessage extends AbstractMessage.AbstractClientMessage<OpenGuiMessage>
{
    private int guiId;

    public OpenGuiMessage() {/* Required by the PacketDispatcher */}

    public OpenGuiMessage(int guiId)
    {
        this.guiId = guiId;
    }


    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        guiId = buffer.readInt();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(guiId);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        player.openGui(Main.instance, guiId, player.world, 0, 0, 0);
    }
}
