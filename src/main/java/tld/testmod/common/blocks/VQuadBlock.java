package tld.testmod.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tld.testmod.ModLogger;

public abstract class VQuadBlock extends Block 
{

    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyEnum<VQuadBlock.EnumPartType> PART = PropertyEnum.<VQuadBlock.EnumPartType> create("part", VQuadBlock.EnumPartType.class);
    
    public VQuadBlock(Material materialIn)
    {
        super(materialIn);
        this.setSoundType(SoundType.WOOD);
        setHardness(0.2F);
        disableStats();
        setDefaultState(this.blockState.getBaseState().withProperty(PART, VQuadBlock.EnumPartType.LL));
    }
    
    public VQuadBlock()
    {
        this(Material.WOOD);
    }
    
    /**
     * 
     * @return Mod ItemBlock or custom Item
     */
    protected abstract Item getItemBlock();   
    
    /*
    * Get the Item that this Block should drop when harvested.
    */
   @Nullable
   @Override
   public Item getItemDropped(IBlockState state, Random rand, int fortune)
   {
       return state.getBlock() == this ? getItemBlock() : null;
   }

   @Override
   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
   {
       return player.capabilities.isCreativeMode ? new ItemStack(getItemBlock()) : ItemStack.EMPTY;
   }

   @Override
   public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, ItemStack stack)
   {
       if (!player.capabilities.isCreativeMode)
           super.harvestBlock(worldIn, player, pos, state, te, stack);
   }

    @Override
    public boolean isFullCube(IBlockState state) {return true;}

    /** Used to determine ambient occlusion and culling when rebuilding chunks for render */
    @Override
    public boolean isOpaqueCube(IBlockState state) {return true;}

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing enumfacing = state.getValue(FACING);
        EnumPartType part = state.getValue(PART);

        if (part == VQuadBlock.EnumPartType.UR)
        {
            if ((worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this) || (worldIn.getBlockState(pos.down()).getBlock() != this))
            {
                worldIn.setBlockToAir(pos);
            }
        } else if (part == VQuadBlock.EnumPartType.LR)
        {
            if ((worldIn.getBlockState(pos.offset(enumfacing.rotateYCCW())).getBlock() != this) || (worldIn.getBlockState(pos.up()).getBlock() != this))
            {
                worldIn.setBlockToAir(pos);
            }
        } else if ((worldIn.getBlockState(pos.offset(enumfacing.rotateY())).getBlock() != this) && ((part == VQuadBlock.EnumPartType.UL) || part == VQuadBlock.EnumPartType.LL ))
        {
            worldIn.setBlockToAir(pos);
        }
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     * FACING | PART - 2 bits each
     * @deprecated  Mojang's messing with stuff
     */
    @Deprecated
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        EnumFacing enumfacing = EnumFacing.getHorizontal(meta>>2);
        return this.getDefaultState().withProperty(PART, VQuadBlock.EnumPartType.values()[meta & 3]).withProperty(FACING, enumfacing);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        int i = 0;
        i = i | ((EnumFacing) state.getValue(FACING)).getHorizontalIndex() << 2;
        i = i | ((EnumPartType) state.getValue(PART)).ordinal();
        return i;
    }

    /**
     * Get the actual Block state of this Block at the given position. This
     * applies properties not visible in the metadata, such as fence
     * connections.
     * 
     * @deprecated  Mojang's messing with stuff
     */
    @Deprecated
    @Override
    public IBlockState getActualState(IBlockState stateIn, IBlockAccess worldIn, BlockPos pos)
    {
        IBlockState stateOut = stateIn;
        return stateOut;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed blockstate.
     * 
     * @deprecated  Mojang's messing with stuff
     */
    @Deprecated
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }

    /** Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed blockstate.
     *
     * @deprecated  Mojang's messing with stuff
     */
    @Deprecated
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }
    
    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {FACING, PART});
    }
    
    public enum EnumPartType implements IStringSerializable
    {
        LL("lower_left"),
        LR("lower_right"),
        UL("upper_left"),
        UR("upper_right");

        private final String name;

        private EnumPartType(String name) {this.name = name;}

        @Override
        public String toString() {return this.name;}

        @Override
        public String getName() {return this.name;}
    }

}
