package tld.testmod.common.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.common.animation.OneShotBlock;

public class BlockPull extends Block
{
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    
    protected static final AxisAlignedBB PULL_FULL_EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.1875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB PULL_FULL_WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB PULL_FULL_SOUTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB PULL_FULL_NORTH_AABB = new AxisAlignedBB(0.375D, 0.0D, 0.8125D, 0.625D, 1.0D, 1.0D);

    protected static final AxisAlignedBB PULL_FULL_EAST_AABB_ON = new AxisAlignedBB(0.0D, 0.0D, 0.375D, 0.1875D, 1.0D, 0.625D);
    protected static final AxisAlignedBB PULL_FULL_WEST_AABB_ON = new AxisAlignedBB(0.8125D, 0.0D, 0.375D, 1.0D, 1.0D, 0.625D);
    protected static final AxisAlignedBB PULL_FULL_SOUTH_AABB_ON = new AxisAlignedBB(0.375D, 0.0D, 0.0D, 0.625D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB PULL_FULL_NORTH_AABB_ON = new AxisAlignedBB(0.375D, 0.0D, 0.8125D, 0.625D, 1.0D, 1.0D);
    
    private final boolean rope;

    public BlockPull(boolean rope)
    {
        super(Material.CARPET);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
        this.setTickRandomly(true);
        this.setCreativeTab(Main.MODTAB);
        this.rope = rope;
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos)
    {
        return NULL_AABB; //this.getBoundingBox(blockState, worldIn, pos);
    }

    /**
     * How many world ticks before ticking
     */
    @Override
    public int tickRate(World worldIn)
    {
        return this.rope ? 15 : 10;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks
     * for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
//    @Override
//    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
//    {
//        return canPlaceBlock(worldIn, pos, side.getOpposite());
//    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
    {
        for (EnumFacing enumfacing : EnumFacing.values())
        {
            if (canPlaceBlock(worldIn, pos, enumfacing)) { return true; }
        }

        return false;
    }

    /**
     * Check whether this block can be placed on the block in the given
     * direction.
     */
    protected static boolean canPlaceBlock(World worldIn, BlockPos pos, EnumFacing direction)
    {
        BlockPos blockpos = pos.offset(direction);
        return direction.equals(EnumFacing.UP) && (worldIn.getBlockState(blockpos).getBlock() instanceof OneShotBlock || worldIn.getBlockState(blockpos).getBlock() instanceof BlockPull);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to
     * allow for adjustments to the IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, Boolean.valueOf(false));

    }

    /**
     * Called when a neighboring block was changed and marks that this state
     * should perform any checks during a neighbor change. Cases may include
     * when redstone power is updated, cactus blocks popping off due to a
     * neighboring solid block, etc.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (this.checkForDrop(worldIn, pos, state) && !canPlaceBlockAt(worldIn, pos))
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
        }
    }

    private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
    {
        if (this.canPlaceBlockAt(worldIn, pos))
        {
            return true;
        } else
        {
            this.dropBlockAsItem(worldIn, pos, state, 0);
            worldIn.setBlockToAir(pos);
            return false;
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
        boolean flag = ((Boolean)state.getValue(POWERED)).booleanValue();

        switch (enumfacing)
        {
            case EAST:
                return flag ? PULL_FULL_EAST_AABB_ON : PULL_FULL_EAST_AABB;
            case WEST:
                return flag ? PULL_FULL_WEST_AABB_ON : PULL_FULL_WEST_AABB;
            case SOUTH:
                return flag ? PULL_FULL_SOUTH_AABB_ON : PULL_FULL_SOUTH_AABB;
            case UP:
            case DOWN:
            case NORTH:
            default:
                return flag ? PULL_FULL_NORTH_AABB_ON : PULL_FULL_NORTH_AABB;
        }
    }

    /**
     * Called when the block is right clicked by a player.
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (playerIn.isSneaking()) return false;
        if (state.getValue(POWERED).booleanValue())
        {
            return true;
        } else
        {
            propagate(worldIn, pos, state, facing, true);
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
            return true;
        }
    }

    protected void playClickSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos)
    {
        worldIn.playSound(player, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.3F, 0.6F);
    }

    protected void playReleaseSound(World worldIn, BlockPos pos)
    {
        worldIn.playSound((EntityPlayer)null, pos, SoundEvents.BLOCK_WOOD_BUTTON_CLICK_OFF, SoundCategory.BLOCKS, 0.3F, 0.5F);
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but
     * before the Tile Entity is updated
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        if (state.getValue(POWERED).booleanValue())
        {
            this.notifyNeighbors(worldIn, pos, state.getValue(FACING));
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        // Only power the bottom face
        return blockState.getValue(POWERED).booleanValue() && side.equals(EnumFacing.DOWN) ? 15 : 0;
//        return ((Boolean)blockState.getValue(POWERED)).booleanValue() ? 15 : 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
//        return !blockState.getValue(POWERED).booleanValue() ? 0 : (blockState.getValue(FACING) == side ? 15 : 0);
        return !blockState.getValue(POWERED).booleanValue() ? 0 : (side.equals(EnumFacing.DOWN) ? 15 : 0);

    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    /**
     * Called randomly when setTickRandomly is set to true (used by e.g. crops
     * to grow, etc.)
     */
    @Override
    public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random)
    {
    }

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (state.getValue(POWERED).booleanValue())
            {
                if (this.rope)
                {
                    this.checkPressed(state, worldIn, pos);
                } else
                {
                    this.propagate(worldIn, pos, state, state.getValue(FACING), false);
                }
            }
        }
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        if (!worldIn.isRemote)
        {
            if (this.rope)
            {
                if (!state.getValue(POWERED).booleanValue())
                {
                    this.checkPressed(state, worldIn, pos);
                }
            }
        }
    }

    private void checkPressed(IBlockState state, World worldIn, BlockPos pos)
    {
        List<? extends Entity> list = worldIn.<Entity> getEntitiesWithinAABB(EntityArrow.class, state.getBoundingBox(worldIn, pos).offset(pos));
        boolean flag = !list.isEmpty();
        boolean flag1 = state.getValue(POWERED).booleanValue();

        if (flag && !flag1)
        {
            this.propagate(worldIn, pos, state, state.getValue(FACING), true);
        }

        if (!flag && flag1)
        {
            this.propagate(worldIn, pos, state, state.getValue(FACING), false);
        }

        if (flag)
        {
            worldIn.scheduleUpdate(new BlockPos(pos), this, this.tickRate(worldIn));
        }
    }

    private void notifyNeighbors(World worldIn, BlockPos pos, EnumFacing facing)
    {
        worldIn.notifyNeighborsOfStateChange(pos, this, false);
        worldIn.notifyNeighborsOfStateChange(pos.offset(facing.getOpposite()), this, false);
    }

    private void propagate(World worldIn, BlockPos posIn, IBlockState state, EnumFacing facing, boolean power)
    {
        this.setPowerState(worldIn, posIn, state, facing, power);
        this.propagateUp(worldIn, posIn, facing, power);
        this.propagateDown(worldIn, posIn, facing, power);

        if (power)
            this.playClickSound((EntityPlayer) null, worldIn, posIn);
        else
            this.playReleaseSound(worldIn, posIn);        
    }
    
    private boolean propagateUp(World worldIn, BlockPos posIn, EnumFacing facing, boolean power)
    {
        BlockPos posAbove = posIn.up();
        IBlockState state = worldIn.getBlockState(posAbove);
        boolean flag = state.getBlock() instanceof BlockPull;
        if (flag)
        {
            this.propagateUp(worldIn, posAbove, facing, power);
            this.setPowerState(worldIn, posAbove, state, facing, power);
        }
        return !flag;
    }

    private boolean propagateDown(World worldIn, BlockPos posIn, EnumFacing facing, boolean power)
    {
        BlockPos posBelow = posIn.down();
        IBlockState state = worldIn.getBlockState(posBelow);
        boolean flag = state.getBlock() instanceof BlockPull;
        if (flag)
        {
            this.propagateDown(worldIn, posBelow, facing, power);
            this.setPowerState(worldIn, posBelow, state, facing, power);
        }
        return !flag;
    }
    
    private void setPowerState(World worldIn, BlockPos posIn, IBlockState state, EnumFacing facing, boolean power)
    {
        worldIn.setBlockState(posIn, worldIn.getBlockState(posIn).withProperty(POWERED, Boolean.valueOf(power)), 3);
        this.notifyNeighbors(worldIn, posIn, state.getValue(FACING));
        worldIn.markBlockRangeForRenderUpdate(posIn, posIn); 
    }
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta & 3);
        return this.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, Boolean.valueOf((meta & 8) > 0));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
  
        if (state.getValue(POWERED).booleanValue())
        {
            i |= 8;
        }
        return i;
    }

    /**
     * Returns the blockstate with the given rotation from the passed
     * blockstate. If inapplicable, returns the passed blockstate.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If
     * inapplicable, returns the passed blockstate.
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[]
        {
                FACING, POWERED
        });
    }
    
    @Override public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
//        int i = MathHelper.floor((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
//        EnumFacing enumfacing = EnumFacing.getHorizontal(i);
//
//        Boolean powered = state.getValue(POWERED);
//        if(entity.isSneaking())
//            propagate(entity.getEntityWorld(), pos, enumfacing);
//        else
//            ModLogger.info("powered %s", powered);
        return true;
    }
}