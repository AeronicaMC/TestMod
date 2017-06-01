package tld.testmod.common.animation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.init.ModSoundEvents;

public class OneShotBlock extends Block
{

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    
    public OneShotBlock()
    {
        this(Material.WOOD);
        setCreativeTab(Main.MODTAB);
    }
    
    public OneShotBlock(Material materialIn)
    {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof OneShotTileEntity)
            {
                ((OneShotTileEntity)te).click(player.isSneaking());
            }
        }
        return true;
    }
    
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        if(worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof OneShotTileEntity)
            {
                ((OneShotTileEntity)te).triggerOneShot();
            }
        }

        float f = (float)Math.pow(2.0D, (double)(param - 12) / 12.0D);
        worldIn.playSound(null, pos, ModSoundEvents.BELL,  SoundCategory.RECORDS, 3.0F, f );
        worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)param / 24.0D, 0.0D, 0.0D, new int[0]);
        ModLogger.info("eventReceived: %d", param);
        return true;
    }
    
    /**
     * React to a redstone powered neighbor block
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof OneShotTileEntity)
        {
            OneShotTileEntity oneShotTE = (OneShotTileEntity)te;
            if (oneShotTE.isPreviousRedstoneState() != flag)
            {
                if (flag)
                    oneShotTE.triggerOneShot();

                oneShotTE.setPreviousRedstoneState(flag);
            }
        }
    }

    @Override
    public ExtendedBlockState createBlockState()
    {
        return new ExtendedBlockState(this, new IProperty[]{ FACING, Properties.StaticProperty }, new IUnlistedProperty[]{ Properties.AnimationProperty });
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) { return false; }

    @Override
    public boolean isFullCube(IBlockState state) { return false; }

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
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing)state.getValue(FACING)).getIndex();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new OneShotTileEntity();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(Properties.StaticProperty, true);
    }

}
