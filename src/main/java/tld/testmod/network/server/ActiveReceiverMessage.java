package tld.testmod.network.server;

import java.io.IOException;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.library.client.midi.IActiveNoteReceiver;
import tld.testmod.network.AbstractMessage.AbstractServerMessage;

public class ActiveReceiverMessage extends AbstractServerMessage<ActiveReceiverMessage>
{

    BlockPos blockPos;
    int entityId;
    EnumHand hand;
    byte note;
    byte volume;
    
    public ActiveReceiverMessage() { /* empty */ }
    
    public ActiveReceiverMessage(BlockPos pos, int entityId, EnumHand hand, byte note, byte volume)
    {
        this.blockPos = pos;
        this.entityId = entityId;
        this.hand = hand;
        this.note = note;
        this.volume = volume;
    }
    
    @Override
    protected void read(PacketBuffer buffer) throws IOException
    {
        blockPos = BlockPos.fromLong(buffer.readLong());
        entityId = buffer.readInt();
        hand = EnumHand.values()[buffer.readByte()];
        note = buffer.readByte();
        volume = buffer.readByte();
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException
    {
        buffer.writeLong(blockPos.toLong());
        buffer.writeInt(entityId);
        buffer.writeByte(hand.ordinal());
        buffer.writeByte(note);
        buffer.writeByte(volume);
    }

    @Override
    public void process(EntityPlayer player, Side side)
    {
        World world = player.getEntityWorld();
        if (world.isBlockLoaded(blockPos))
        {
            EntityPlayer personPlaying = (EntityPlayer) world.getEntityByID(entityId);
            IBlockState state = world.getBlockState(blockPos);

            if (state.getBlock() instanceof IActiveNoteReceiver)
            {
                IActiveNoteReceiver instrument = (IActiveNoteReceiver) state.getBlock();
                instrument.noteReceiver(world, blockPos, entityId, note, volume);
            } else if (entityId == player.getEntityId() && personPlaying.getHeldItem(hand).getItem() instanceof IActiveNoteReceiver)
            {
                IActiveNoteReceiver instrument = (IActiveNoteReceiver) personPlaying.getHeldItem(hand).getItem();
                instrument.noteReceiver(world, blockPos, entityId, note, volume);
            }
        }
    }

}
