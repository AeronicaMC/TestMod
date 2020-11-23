package tld.testmod.common.blocks;


import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tld.testmod.Main;
import tld.testmod.init.ModBlocks;

import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockEmissive extends Block
{
    public static final PropertyBool POWERED = PropertyBool.create("powered");

    public BlockEmissive()
    {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.FALSE));
        this.setTickRandomly(true);
        this.setCreativeTab(Main.MOD_TAB);
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, POWERED);
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return false;
    }

    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return 0;
    }
    @Override

    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return blockState.getWeakPower(blockAccess, pos, side);
    }

    @Override
    public int tickRate(World worldIn)
    {
        return 10;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        if (!worldIn.isRemote)
        {
            if (state.getValue(POWERED) && !worldIn.isBlockPowered(pos))
            {
                worldIn.setBlockState(pos, ModBlocks.BLOCK_EMISSIVE.getDefaultState(), 2);
            }
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModBlocks.ITEM_EMISSIVE;
    }

    @Override
    public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {

        return (layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.TRANSLUCENT);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getLightValue(IBlockState state)
    {
        return MinecraftForgeClient.getRenderLayer() == BlockRenderLayer.SOLID && state.getValue(POWERED) ? 15 : super.getLightValue(state);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (playerIn.isSneaking()) return false;
        if (!state.getValue(POWERED))
        {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
        return true;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        this.neighborChanged(state, worldIn, pos, null, null);
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote)
        {
            if (state.getValue(POWERED) && !worldIn.isBlockPowered(pos))
            {
                worldIn.scheduleUpdate(pos, this, 4);
            }
            else if (!state.getValue(POWERED) && worldIn.isBlockPowered(pos))
            {
                worldIn.setBlockState(pos, ModBlocks.BLOCK_EMISSIVE.getDefaultState().withProperty(POWERED, Boolean.TRUE), 2);
            }
        }
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(POWERED, Boolean.FALSE);
    }

    @Override
    public IBlockState getActualState(IBlockState stateIn, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState stateOut = stateIn;
        return stateOut;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(POWERED, (meta & 8) > 0);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        if (state.getValue(POWERED))
        {
            i |= 8;
        }
        return i;
    }

}
