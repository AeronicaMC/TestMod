package tld.testmod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.client.midi.IActiveNoteReceiver;
import tld.testmod.client.midi.MidiUtils;

public class BlockCarillon extends Block implements IActiveNoteReceiver
{

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    protected static final AxisAlignedBB CARILLON_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    
    public BlockCarillon()
    {
        super(Material.WOOD);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(Main.MOD_TAB);
    }

    /**
     * React to a redstone powered neighbor block
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof CarillionTileEntity)
        {
            CarillionTileEntity carillonTE = (CarillionTileEntity)te;
            if (carillonTE.isPreviousRedstoneState() != flag)
            {
                if (flag)
                    carillonTE.triggerCarillon();

                carillonTE.setPreviousRedstoneState(flag);
            }
        }
    }


    /* (non-Javadoc)
     * @see net.minecraft.block.Block#onBlockHarvested(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.player.EntityPlayer)
     */
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        notifyRemoved(worldIn, pos);
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    /* (non-Javadoc)
     * @see net.minecraft.block.Block#onBlockActivated(net.minecraft.world.World, net.minecraft.util.math.BlockPos, net.minecraft.block.state.IBlockState, net.minecraft.entity.player.EntityPlayer, net.minecraft.util.EnumHand, net.minecraft.util.EnumFacing, float, float, float)
     */
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        MidiUtils.INSTANCE.setNoteReceiver(this, worldIn, pos, state, playerIn, hand, facing, hitX, hitY, hitZ);
        return true;
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
    }

    @Override
    public IBlockState getActualState(IBlockState stateIn, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState stateOut = stateIn;
        return stateOut;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new CarillionTileEntity();
    }

    @Override
    public void noteReceiver(World worldIn, BlockPos posIn, int EntityId, byte noteIn, byte volumeIn)
    {
        TileEntity te = worldIn.getTileEntity(posIn);

        if (te instanceof CarillionTileEntity)
        {
            CarillionTileEntity carillonTE = (CarillionTileEntity)te;
            carillonTE.play(noteIn, volumeIn);
        }
    }
    
}
