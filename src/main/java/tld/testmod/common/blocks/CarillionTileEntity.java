package tld.testmod.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import tld.testmod.ModLogger;
import tld.testmod.common.animation.OneShotTileEntity;
import tld.testmod.init.ModBlocks;

public class CarillionTileEntity extends TileEntity
{

    private boolean previousRedstoneState;

    public CarillionTileEntity()
    {
    }

    public void triggerCarillon()
    {
        int offset = 3;
        IBlockState state = world.getBlockState(pos);
        EnumFacing facing = (EnumFacing) state.getValue(BlockCarillon.FACING);
        BlockCarillon blockCarillon = (BlockCarillon) state.getBlock();
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos(pos);
        BlockPos.MutableBlockPos opos = new BlockPos.MutableBlockPos(pos);
        IBlockState mState;
        OneShotTileEntity osTE;
        int xx, zz;
        xx = zz = 0;
        byte pitch = 0;
        for (int x=0; x<3; x++)
            for (int z=0; z<3; z++)
                for (int y=0; y<3; y++)
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
                    opos.setPos(pos.offset(facing.getOpposite(), offset).offset(facing.rotateY()).add(xx,y,zz));
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

}
