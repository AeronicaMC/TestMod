/**
 * Copyright {2016} Paul Boese aka Aeronica
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

public class TestAnimEntity extends EntityLiving
{

    private final IAnimationStateMachine asm;
    private final VariableValue cycleLength = new VariableValue(getHealth() / 5);
    
    public TestAnimEntity(World world)
    {
        super(world);
        setSize(1, 1);
        asm = Main.proxy.load(new ResourceLocation(Main.MODID, "asms/block/engine.json"), ImmutableMap.<String, ITimeValue>of(
            "cycle_length", cycleLength
        ));
    }

    public void handleEvents(float time, Iterable<Event> pastEvents)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if (world.isRemote && cycleLength != null)
        {
            cycleLength.setValue(getHealth() / 5);
        }
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
