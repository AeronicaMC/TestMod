package tld.testmod.common.animation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import tld.testmod.Main;

public class OneShotItemAnimationHolder implements ICapabilityProvider
{

    private final VariableValue clickTime = new VariableValue(Float.NEGATIVE_INFINITY);
    private final VariableValue cycleLength = new VariableValue(4);

    private final IAnimationStateMachine asm = Main.proxy.load(new ResourceLocation(Main.MODID, "asms/block/one_shot.json"), ImmutableMap.<String, ITimeValue>of(
         "click_time", clickTime,
         "cycle_length", cycleLength
    ));

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityAnimation.ANIMATION_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if(capability == CapabilityAnimation.ANIMATION_CAPABILITY)
        {
            return CapabilityAnimation.ANIMATION_CAPABILITY.cast(asm);
        }
        return null;
    }

}
