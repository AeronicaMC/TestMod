package tld.testmod.client.midi;

import javax.sound.midi.MidiMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IMidiIn
{
    public void midiSend(World worldIn, BlockPos posIn, IBlockState stateIn, EntityPlayer playerIn, MidiMessage msg);
}
