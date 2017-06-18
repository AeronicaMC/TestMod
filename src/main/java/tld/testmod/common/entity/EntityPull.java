package tld.testmod.common.entity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tld.testmod.common.blocks.BlockPull;
import tld.testmod.init.ModBlocks;

public class EntityPull extends EntityThrowable
{
    
    public EntityPull(World worldIn)
    {
        super(worldIn);
    }

    public EntityPull(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityPull(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 8; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.SNOWBALL, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            int i = 0;

            if (result.entityHit instanceof EntityBlaze)
            {
                i = 3;
            }

            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)i);
        }

        if (!this.world.isRemote)
        {
            this.placePull(this.world, this.getThrower(), new Vec3d(this.posX, this.posY, this.posZ));
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }

    private void placePull(World worldIn, EntityLivingBase thrower, Vec3d posIn)
    {
        Vec3d posInBelow = posIn.addVector(0, -1.0D, 0);
        BlockPos pos = new BlockPos(posIn);
        IBlockState state = worldIn.getBlockState(pos);
        boolean flag = state.getBlock().isAir(state, worldIn, pos) || state.getBlock().isLeaves(state, worldIn, pos);

        if (flag)
        {
            IBlockState state2 = worldIn.getBlockState(pos.up());
            boolean flag2 = state2.getBlock() instanceof BlockPull;
            EnumFacing facing = flag2 ? state2.getValue(BlockPull.FACING) : thrower.getAdjustedHorizontalFacing();
            if (ModBlocks.PULL_ROPE.canPlaceBlockAt(worldIn, pos))
                worldIn.setBlockState(pos, ModBlocks.PULL_ROPE.getDefaultState().withProperty(BlockPull.FACING, facing).withProperty(BlockPull.POWERED, Boolean.valueOf(false)));
            this.placePull(worldIn, thrower, posInBelow);
        }
    }

}
