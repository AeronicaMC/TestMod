package tld.testmod.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
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
        int cubeSize = 11;
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

    public void play(byte noteIn, byte volumeIn)
    {
        byte note = (byte) (noteIn - 48);
        boolean noteOn = (volumeIn != 0);

        if (noteOn)
        {
            int cubeSize = 11;
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
                                        }
                                    if (pitch > 24) break;
                                }
                            }
                        }
                    }
        }

    }

    /**
     * 1.9.4 TE Syncing
     * https://gist.github.com/williewillus/7945c4959b1142ece9828706b527c5a4
     * 
     * When the chunk/block data is sent:
     * 
     * - getUpdateTag() called to get compound to sync - this tag must include
     * coordinate and id tags - vanilla TE's write ALL data into this tag by
     * calling writeToNBT
     * 
     * When TE is resynced:
     * 
     * - getUpdatePacket() called to get a SPacketUpdateTileEntity (this is more
     * limited than it used to) - the packet itself holds the pos, compound
     * itself need not include coordinates - compound can contain whatever you'd
     * like, since it just comes back to you in onDataPacket() - vanilla just
     * delegates to getUpdateTag(), writing ALL te data, coordinates, and id
     * into the packet, and reading it all out on the other side - but mods
     * don't have to
     * 
     */
    @Override
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound tag = super.getUpdateTag();
        return this.writeToNBT(tag);
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        NBTTagCompound cmp = new NBTTagCompound();
        writeToNBT(cmp);
        return new SPacketUpdateTileEntity(pos, 1, cmp);
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet)
    {
        readFromNBT(packet.getNbtCompound());
    }

}
