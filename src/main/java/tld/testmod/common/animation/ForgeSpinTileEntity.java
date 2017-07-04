package tld.testmod.common.animation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import tld.testmod.Main;

public class ForgeSpinTileEntity extends TileEntity implements ITickable
{

    @Nullable
    private final IAnimationStateMachine asm;
    private final VariableValue cycle = new VariableValue(0);
    int tickcounter;
    
    public ForgeSpinTileEntity()
    {
        asm = Main.proxy.load(new ResourceLocation(Main.MODID, "asms/block/forge_spin_asm.json"), ImmutableMap.<String, ITimeValue>of(
                "cycle", cycle
            ));
    }

    @Override
    public void update()
    {
        tickcounter++;
        if (world.isRemote) {
            cycle.setValue(tickcounter/40.0F);
        }   
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

}
