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
package tld.testmod.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tld.testmod.ModLogger;

public class VQuadBlock extends Block 
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
            if ((worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) || (worldIn.getBlockState(pos.down()).getBlock() != this))
            {
                worldIn.setBlockToAir(pos);
            }
        } else if (part == VQuadBlock.EnumPartType.LR)
        {
            if ((worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this) || (worldIn.getBlockState(pos.up()).getBlock() != this))
            {
                worldIn.setBlockToAir(pos);
            }
        } else if ((worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this) && ((part == VQuadBlock.EnumPartType.UL) || part == VQuadBlock.EnumPartType.LL ))
        {
            worldIn.setBlockToAir(pos);

            if (!worldIn.isRemote)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                ModLogger.info("VBQTest Drop %s", state);
            }
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
