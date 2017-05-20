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
package tld.testmod.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.common.blocks.HQBTest;

public class HQBTestItemBlock extends Item
{
    public HQBTestItemBlock()
    {
        setMaxStackSize(1);
        setCreativeTab(Main.MODTAB);
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos posIn, EnumHand handIn, EnumFacing facingIn, float hitX, float hitY, float hitZ)
    {
        BlockPos posFL = posIn;
        if (worldIn.isRemote)
        {
            /** Client side so just return */
            return EnumActionResult.SUCCESS;
        } else if (facingIn != EnumFacing.UP)
        {
            /** Can't place the blocks this way */
            return EnumActionResult.FAIL;
        } else
        {
            IBlockState iblockstate = worldIn.getBlockState(posFL);
            Block block = iblockstate.getBlock();
            ItemStack stack = playerIn.getHeldItem(handIn);
            /** Looking at the ground or a replaceable block like grass. */
            boolean flagLL = block.isReplaceable(worldIn, posFL);
            if (!flagLL) posFL = posFL.up();

            /**determine the direction the player is facing */
            int i = MathHelper.floor((double) (playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.getHorizontal(i);
            /**get the next blocks. */
            BlockPos posFR = posFL.offset(enumfacing);
            BlockPos posBL = posFL.offset(enumfacing.rotateYCCW());
            BlockPos posBR = posBL.offset(enumfacing);

            if (playerIn.canPlayerEdit(posFL, facingIn, stack) && playerIn.canPlayerEdit(posFR, facingIn, stack) &&
                    playerIn.canPlayerEdit(posBL, facingIn, stack) && playerIn.canPlayerEdit(posBR, facingIn, stack))
            {
                boolean flagLR = worldIn.getBlockState(posFR).getBlock().isReplaceable(worldIn, posFR);
                boolean flagUL = worldIn.getBlockState(posBL).getBlock().isReplaceable(worldIn, posBL);
                boolean flagUR = worldIn.getBlockState(posBR).getBlock().isReplaceable(worldIn, posBR);
                boolean flag2 = flagLL || worldIn.isAirBlock(posFL);
                boolean flag3 = flagLR || worldIn.isAirBlock(posFR);
                boolean flag4 = flagUL || worldIn.isAirBlock(posBL);
                boolean flag5 = flagUR || worldIn.isAirBlock(posBR);
                

                /** Disallow placing blocks on water or other unstable blocks */
                if (flag2 && flag3 && flag4 && flag5 && worldIn.getBlockState(posFL.down()).isFullyOpaque() && worldIn.getBlockState(posFR.down()).isFullyOpaque() &&
                        worldIn.getBlockState(posBL.down()).isFullyOpaque() && worldIn.getBlockState(posBR.down()).isFullyOpaque())
                {
                    IBlockState iblockstate1 = tld.testmod.common.ModBlocks.BLOCK_HQBTEST.getDefaultState().withProperty(HQBTest.FACING, enumfacing)
                            .withProperty(HQBTest.PART, HQBTest.EnumPartType.FL);

                    if (worldIn.setBlockState(posFL, iblockstate1, 11))
                    {
                        IBlockState iblockstate2 = iblockstate1.withProperty(HQBTest.PART, HQBTest.EnumPartType.FR);
                        if (worldIn.setBlockState(posFR, iblockstate2, 11))
                        {
                            IBlockState iblockstate3 = iblockstate2.withProperty(HQBTest.PART, HQBTest.EnumPartType.BL);
                            if (worldIn.setBlockState(posBL, iblockstate3, 11))
                            {
                                IBlockState iblockstate4 = iblockstate2.withProperty(HQBTest.PART, HQBTest.EnumPartType.BR);
                                worldIn.setBlockState(posBR, iblockstate4, 11);
                            }
                        }
                    }

                    SoundType soundtype = iblockstate1.getBlock().getSoundType();
                    worldIn.playSound((EntityPlayer) null, posFL, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
                    stack.setCount(stack.getCount()-1);
                    return EnumActionResult.SUCCESS;
                } else
                {
                    return EnumActionResult.FAIL;
                }
            } else
            {
                return EnumActionResult.FAIL;
            }
        }
    }

}
