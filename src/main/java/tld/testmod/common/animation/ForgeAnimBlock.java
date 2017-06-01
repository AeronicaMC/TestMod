package tld.testmod.common.animation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import tld.testmod.Main;

public class ForgeAnimBlock extends Block
{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    
    public ForgeAnimBlock()
    {
        this(Material.WOOD);
        setCreativeTab(Main.MODTAB);
    }
   
    public ForgeAnimBlock(Material materialIn)
    {
        super(materialIn);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof ForgeAnimTileEntity)
            {
                ((ForgeAnimTileEntity)te).click(player.isSneaking());
            }
        }
        return true;
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
        return this.getDefaultState().withProperty(FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
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
        return new ForgeAnimTileEntity();
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.withProperty(Properties.StaticProperty, true);
    }
    
}
