package tld.testmod.network.server;

import java.io.IOException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.client.midi.IActiveNoteReceiver;
import tld.testmod.network.AbstractMessage.AbstractServerMessage;

public class ActiveReceiverMessage extends AbstractServerMessage<ActiveReceiverMessage>
{

    int posX, posY, posZ;
    int entityId;
    byte note;
    byte volume;
    
    public ActiveReceiverMessage() { /* empty */ }
    
    public ActiveReceiverMessage(BlockPos pos, int entityId, byte note, byte volume)
    {
        this.posX = pos.getX();
        this.posY = pos.getY();
        this.posZ = pos.getZ();
        this.entityId = entityId;
        this.note = note;
        this.volume = volume;
    }
    
    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        posX = buffer.readInt();
        posY = buffer.readInt();
        posZ = buffer.readInt();
        entityId = buffer.readInt();
        note = buffer.readByte();
        volume = buffer.readByte();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeInt(posX);
        buffer.writeInt(posY);
        buffer.writeInt(posZ);
        buffer.writeInt(entityId);
        buffer.writeByte(note);
        buffer.writeByte(volume);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        World world = player.getEntityWorld();
        BlockPos pos = new BlockPos(posX, posY, posZ);
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() instanceof IActiveNoteReceiver)
        {
            IActiveNoteReceiver instrument = (IActiveNoteReceiver) state.getBlock();
            instrument.noteReceiver(world, pos, entityId, note, volume);
        } else if (entityId == player.getEntityId() && player.getHeldItemMainhand().getItem() instanceof IActiveNoteReceiver)
        {
            IActiveNoteReceiver instrument = (IActiveNoteReceiver) player.getHeldItemMainhand().getItem();
            instrument.noteReceiver(world, pos, entityId, note, volume);
        }
    }

}
