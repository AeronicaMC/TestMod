package tld.testmod.common.animation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import tld.testmod.Main;

public class ForgeSpinEntity extends EntityLiving
{

    private final IAnimationStateMachine asm;
    private final VariableValue cycle = new VariableValue(getHealth() / 5);
    int tickcounter;
    
    public ForgeSpinEntity(World world)
    {
        super(world);
        setSize(1, 1);
        asm = Main.proxy.load(new ResourceLocation(Main.MODID, "asms/block/forge_spin_asm.json"), ImmutableMap.<String, ITimeValue>of(
            "cycle", cycle
        ));
    }

    public void handleEvents(float time, Iterable<Event> pastEvents)
    {
        // Nothing to do here
    }

    @Override
    public void onEntityUpdate()
    {
        tickcounter++;
        if (world.isRemote && cycle != null)
        {
            cycle.setValue(tickcounter / 40.0F);
        }
        super.onEntityUpdate();
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60);
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
