package tld.testmod.common.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import tld.testmod.ModLogger;

public class CarillionTileEntity extends TileEntity
{

    private boolean previousRedstoneState;

    public CarillionTileEntity()
    {
    }

    public void triggerCarillon()
    {
        ModLogger.info("Carillon triggered! %s", world.isRemote ? "client" : "server");
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
