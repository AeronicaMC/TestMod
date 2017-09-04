package tld.testmod.common.entity;

import java.util.Timer;
import java.util.TimerTask;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tld.testmod.Main;
import tld.testmod.common.blocks.BlockPull;
import tld.testmod.init.ModBlocks;
import tld.testmod.init.ModItems;

public class EntityPull extends EntityThrowable
{
    
    ItemStack stackIn = ItemStack.EMPTY;
    boolean isCreative = true;
    boolean stuckOne = false;
    
    public EntityPull(World worldIn)
    {
        super(worldIn);
    }

    public EntityPull(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityPull(World worldIn, EntityLivingBase throwerIn, ItemStack stackIn, boolean isCreativeIn)
    {
        super(worldIn, throwerIn);
        this.stackIn = stackIn;
        this.isCreative = isCreativeIn;
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
                Main.proxy.spawnRopeParticle(world, this.posX, this.posY, this.posZ);
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), (float)1);
        }

        if (!this.world.isRemote)
        {
            stuckOne = placePull(this.world, this.getThrower(), stackIn ,new Vec3d(this.posX, this.posY, this.posZ), isCreative);
            if(stuckOne && !isCreative)
                stackIn.grow(1);
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }

    private static boolean hasRope(EntityLivingBase thrower, boolean isCreative)
    {
        int ropeCount = 0;
        if (isCreative || !(thrower instanceof EntityPlayer))
            return true;
        for (int i = 0; i < 36; i++) {
            final ItemStack stack = ((EntityPlayer)thrower).inventory.mainInventory.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            final Item item = stack.getItem();
            if (item.equals(ModItems.ITEM_PULL)) {
                ropeCount += stack.getCount();
            }
        }
        return ropeCount > 0;
    }
    
    private static void useRope(EntityLivingBase thrower, boolean isCreative)
    {
        if (isCreative || !(thrower instanceof EntityPlayer))
            return;
        for (int i = 0; i < 36; i++) {
            final ItemStack stack = ((EntityPlayer)thrower).inventory.mainInventory.get(i);
            if (stack.isEmpty()) {
                continue;
            }
            final Item item = stack.getItem();
            if (item.equals(ModItems.ITEM_PULL)) {
                if (stack.getCount() > 0)
                {
                    stack.shrink(1);
                    return;
                }
            }
        }       
    }
    
    private static boolean placePull(World worldIn, EntityLivingBase thrower, ItemStack stackIn, Vec3d posIn, boolean isCreative)
    {
        Vec3d posInBelow = posIn.addVector(0, -1.0D, 0);
        BlockPos pos = new BlockPos(posIn);
        IBlockState state = worldIn.getBlockState(pos);
        boolean stuckOne = false;
        boolean flag = (state.getBlock().isAir(state, worldIn, pos) || state.getBlock().isLeaves(state, worldIn, pos)) &&
                hasRope((EntityPlayer) thrower, isCreative);

        if (flag)
        {
            IBlockState state2 = worldIn.getBlockState(pos.up());
            boolean flag2 = state2.getBlock() instanceof BlockPull;
            EnumFacing facing = flag2 ? state2.getValue(BlockPull.FACING) : thrower.getAdjustedHorizontalFacing();
            if (ModBlocks.PULL_ROPE.canPlaceBlockAt(worldIn, pos))
            {
                useRope((EntityPlayer)thrower, isCreative);
                stuckOne = true;
                worldIn.setBlockState(pos, ModBlocks.PULL_ROPE.getDefaultState().withProperty(BlockPull.FACING, facing).withProperty(BlockPull.POWERED, Boolean.valueOf(false)));
            }
            new Timer().schedule(new TimerTask() {

                @Override
                public void run() {
                    placePull(worldIn, thrower, stackIn, posInBelow, isCreative);
                }
            }, 100);

        }
        return stuckOne;
    }

}
