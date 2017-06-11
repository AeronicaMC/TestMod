package tld.testmod.common.blocks;

import javax.sound.midi.MidiMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import tld.testmod.ModLogger;
import tld.testmod.common.animation.OneShotTileEntity;
import tld.testmod.init.ModBlocks;
import tld.testmod.init.ModSoundEvents;

public class CarillionTileEntity extends TileEntity
{

    private boolean previousRedstoneState;

    public CarillionTileEntity()
    {
    }

    public void triggerCarillon()
    {
        int cubeSize = 10;
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = (EnumFacing) state.getValue(BlockCarillon.FACING);
        BlockPos.MutableBlockPos opos = new BlockPos.MutableBlockPos(pos);
        IBlockState mState;
        OneShotTileEntity osTE;
        int xx, zz;
        xx = zz = 0;
        byte pitch = 0;
        for (int x=0; x<cubeSize; x++)
            for (int z=0; z<cubeSize; z++)
                for (int y=0; y<cubeSize; y++)
                {
                    switch (facing)
                    {
                    case SOUTH:
                        xx=x; zz=z;
                        break;
                    case EAST:
                        xx=x; zz=-z;
                        break;
                    case WEST:
                        xx=-x; zz=z;
                        break;
                    case NORTH:
                    default:
                        xx=-x; zz=-z;                            
                    }
                    opos.setPos(pos.offset(facing.getOpposite(), cubeSize).offset(facing.rotateY()).add(xx,y,zz));
                    mState = world.getBlockState(opos);
                    if (mState.getBlock() == ModBlocks.ONE_SHOT)
                    {
                        if (ModBlocks.ONE_SHOT.hasTileEntity(mState))
                        {
                            if((osTE = ((OneShotTileEntity) world.getTileEntity(opos))) != null)
                            {
                                osTE.setPitch(pitch++);
                                ModLogger.info("OneShot Bell Found! %s, %s, %d", mState, opos, osTE.getPitch());
                                osTE.triggerOneShot();
                                if (pitch > 24) break;
                            }
                        }
                    }
                }
        
    }

    /**
     * @return the previousRedstoneState
     */
    public boolean isPreviousRedstoneState()
    {
        return previousRedstoneState;
    }

    /**
     * @param previousRedstoneState
     *            the previousRedstoneState to set
     */
    public void setPreviousRedstoneState(boolean previousRedstoneState)
    {
        this.previousRedstoneState = previousRedstoneState;
        markDirty();
    }

    // Persistence and syncing to client
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        previousRedstoneState = tag.getBoolean("powered");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setBoolean("powered", this.previousRedstoneState);
        return super.writeToNBT(tag);
    }

    public void play(MidiMessage msg)
    {
        byte[] message = msg.getMessage();
        byte note = (byte) (message[1] - 48);
        boolean noteOn = (message[2] != 0);

        if (noteOn)
        {
            int cubeSize = 10;
            IBlockState state = world.getBlockState(pos);
            EnumFacing facing = (EnumFacing) state.getValue(BlockCarillon.FACING);
            BlockPos.MutableBlockPos opos = new BlockPos.MutableBlockPos(pos);
            IBlockState mState;
            OneShotTileEntity osTE;
            int xx, zz;
            xx = zz = 0;
            byte pitch = 0;
            for (int x=0; x<cubeSize; x++)
                for (int z=0; z<cubeSize; z++)
                    for (int y=0; y<cubeSize; y++)
                    {
                        switch (facing)
                        {
                        case SOUTH:
                            xx=x; zz=z;
                            break;
                        case EAST:
                            xx=x; zz=-z;
                            break;
                        case WEST:
                            xx=-x; zz=z;
                            break;
                        case NORTH:
                        default:
                            xx=-x; zz=-z;                            
                        }
                        opos.setPos(pos.offset(facing.getOpposite(), cubeSize).offset(facing.rotateY()).add(xx,y,zz));
                        mState = world.getBlockState(opos);
                        if (mState.getBlock() == ModBlocks.ONE_SHOT)
                        {
                            if (ModBlocks.ONE_SHOT.hasTileEntity(mState))
                            {
                                if((osTE = ((OneShotTileEntity) world.getTileEntity(opos))) != null)
                                {
                                    osTE.setPitch(pitch++);
                                    if (note == osTE.getPitch())
                                        {
                                        // this is only for some local testing. To make this work correctly midiIn packets will need to be sent to the server.
                                            osTE.triggerOneShot();
                                            float f = (float)Math.pow(2.0D, (double)(osTE.getPitch() - 12) / 12.0D);
                                            Minecraft.getMinecraft().player.playSound(ModSoundEvents.BELL, 3.0F, f );
                                            world.addBlockEvent(pos, ModBlocks.ONE_SHOT, 1, osTE.getPitch());
                                        }
                                    if (pitch > 24) break;
                                }
                            }
                        }
                    }
        }

    }

}
