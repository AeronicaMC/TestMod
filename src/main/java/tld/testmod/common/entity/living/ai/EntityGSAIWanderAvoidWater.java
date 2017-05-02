package tld.testmod.common.entity.living.ai;

import javax.annotation.Nullable;

import net.minecraft.entity.EntityCreature;
import net.minecraft.util.math.Vec3d;

public class EntityGSAIWanderAvoidWater extends EntityGSAIWander
{
    private final float probability;

    public EntityGSAIWanderAvoidWater(EntityCreature p_i47301_1_, double p_i47301_2_)
    {
        this(p_i47301_1_, p_i47301_2_, 0.001F);
    }

    public EntityGSAIWanderAvoidWater(EntityCreature p_i47302_1_, double p_i47302_2_, float p_i47302_4_)
    {
        super(p_i47302_1_, p_i47302_2_);
        this.probability = p_i47302_4_;
    }

    @Nullable
    protected Vec3d getPosition()
    {
        if (this.entity.isInWater())
        {
            Vec3d vec3d = RandomGSPositionGenerator.getLandPos(this.entity, 15, 7);
            return vec3d == null ? super.getPosition() : vec3d;
        }
        else
        {
            return this.entity.getRNG().nextFloat() >= this.probability ? RandomGSPositionGenerator.getLandPos(this.entity, 10, 7) : super.getPosition();
        }
    }
}