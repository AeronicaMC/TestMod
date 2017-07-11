package tld.testmod.common.animation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.init.ModBlocks;

public class TestAnimTileEntity extends TileEntity
{

    private boolean openState = false;
    private boolean previousRedstoneState;
    
    @Nullable
    private final IAnimationStateMachine asm;
    private final VariableValue clickTime = new VariableValue(Float.NEGATIVE_INFINITY);
    
    public TestAnimTileEntity()
    {
        asm = Main.proxy.load(new ResourceLocation(Main.MODID, "asms/block/test_anim.json"), ImmutableMap.<String, ITimeValue>of(
                "click_time", clickTime
            ));
    }
    
    /* (non-Javadoc)
     * @see net.minecraft.tileentity.TileEntity#setWorld(net.minecraft.world.World)
     */
    @Override
    public void setWorld(World worldIn)
    {
        if (worldIn.isRemote && asm != null)
            asm.transition(openState ? "open" : "closed");
        super.setWorld(worldIn);
    }

    /* (non-Javadoc)
     * @see net.minecraft.tileentity.TileEntity#setWorldCreate(net.minecraft.world.World)
     */
    @Override
    protected void setWorldCreate(World worldIn)
    {
        if (worldIn.isRemote && asm != null)
            asm.transition(openState ? "open" : "closed");
        super.setWorld(worldIn);
    }

    public void handleEvents(float time, Iterable<Event> pastEvents)
    {
        for(Event event : pastEvents)
        {
            System.out.println("Event: " + event.event() + " " + event.offset() + " " + getPos() + " " + time);
        }
    }
    
    @Override
    public boolean hasFastRenderer()
    {
        return true;
    }

    public void click(boolean isOpen)
    {
        if (!this.getWorld().isRemote)
        {
            openState = !openState;
            setOpenState(openState);
            this.getWorld().addBlockEvent(pos, ModBlocks.TEST_ANIM, 0, openState ? 1 : 0 );
        }

        if(this.getWorld().isRemote) {
            if (asm != null)
            {
                if(!isOpen) {
                    float time = Animation.getWorldTime(getWorld(), Animation.getPartialTickTime());
                    clickTime.setValue(time);
                    asm.transition("closing");
                    ModLogger.info("click closing: %f", time);
                } else {
                    float time = Animation.getWorldTime(getWorld(), Animation.getPartialTickTime());
                    clickTime.setValue(time);
                    asm.transition("opening");
                    ModLogger.info("click opening: %f", time);
                }
            }
        }  
    }
    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing side)
    {
        if(capability == CapabilityAnimation.ANIMATION_CAPABILITY)
        {
            return true;
        }
        return super.hasCapability(capability, side);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing side)
    {
        if(capability == CapabilityAnimation.ANIMATION_CAPABILITY)
        {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return super.getCapability(capability, side);
    }

    /**
     * @return the previousRedstoneState
     */
    public boolean isPreviousRedstoneState()
    {
        return previousRedstoneState;
    }

    /**
     * @param previousRedstoneState the previousRedstoneState to set
     */
    public void setPreviousRedstoneState(boolean previousRedstoneState)
    {
        this.previousRedstoneState = previousRedstoneState;
        syncToClient();
    }
    
    /**
     * @return the pitch
     */
    public boolean getOpenState()
    {
        return openState;
    }

    /**
     * @param pitch the pitch to set
     */
    public void setOpenState(boolean openState)
    {
        this.openState = openState;
        syncToClient();
    }

    // Persistence and syncing to client
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        openState = tag.getBoolean("open_state");
        previousRedstoneState = tag.getBoolean("powered");
        if (this.getWorld().isRemote && asm != null )
            asm.transition(openState ? "open" : "closed");
            
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setBoolean("open_state", openState);
        tag.setBoolean("powered", this.previousRedstoneState);
        return super.writeToNBT(tag);
    }

    public void syncToClient()
    {
        this.markDirty();
        if (world != null)
        {
            if (!this.getWorld().isRemote && !this.isInvalid())
            {
                IBlockState state = this.getWorld().getBlockState(this.getPos());
                /**
                 * Sets the block state at a given location. Flag 1 will cause a
                 * block update. Flag 2 will send the change to clients (you
                 * almost always want this). Flag 4 prevents the block from
                 * being re-rendered, if this is a client world. Flags can be
                 * added together.
                 */
                this.getWorld().notifyBlockUpdate(getPos(), state, state, 3);
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
        this.writeToNBT(cmp);
        return new SPacketUpdateTileEntity(pos, 1, cmp);
    }

    @Override
    public void onDataPacket(NetworkManager manager, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

}
