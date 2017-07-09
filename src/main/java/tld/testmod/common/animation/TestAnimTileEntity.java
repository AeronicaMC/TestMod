package tld.testmod.common.animation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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

    byte pitch = 0;
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
//    @Override
//    public void setWorld(World worldIn)
//    {
//        if (worldIn.isRemote)
//            asm.transition("rest");
//        super.setWorld(worldIn);
//    }

    /* (non-Javadoc)
     * @see net.minecraft.tileentity.TileEntity#setWorldCreate(net.minecraft.world.World)
     */
//    @Override
//    protected void setWorldCreate(World worldIn)
//    {
//        if (worldIn.isRemote)
//            asm.transition("rest");
//        super.setWorldCreate(worldIn);
//    }

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

    public void click()
    {
        if(this.getWorld().isRemote) {
            if (asm != null)
            {
                if(asm.currentState().equals("open")) {
                    float time = Animation.getWorldTime(getWorld(), Animation.getPartialTickTime());
                    clickTime.setValue(time);
                    asm.transition("closing");
                    ModLogger.info("click closing: %f", time);
                } else if(asm.currentState().equals("closed")) {
                    float time = Animation.getWorldTime(getWorld(), Animation.getPartialTickTime());
                    clickTime.setValue(time);
                    asm.transition("opening");
                    ModLogger.info("click opening: %f", time);
                }
            }
        } else {
            this.getWorld().addBlockEvent(pos, ModBlocks.TEST_ANIM, 1, pitch);
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
        markDirty();
    }
    
    /**
     * @return the pitch
     */
    public byte getPitch()
    {
        return pitch;
    }

    /**
     * @param pitch the pitch to set
     */
    public void setPitch(byte pitch)
    {
        this.pitch = pitch;
        markDirty();
    }

    // Persistence and syncing to client
    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);
        pitch = tag.getByte("pitch");
        pitch = (byte)MathHelper.clamp(pitch, 0, 24);
        previousRedstoneState = tag.getBoolean("powered");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setByte("pitch", pitch);
        tag.setBoolean("powered", this.previousRedstoneState);
        return super.writeToNBT(tag);
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
