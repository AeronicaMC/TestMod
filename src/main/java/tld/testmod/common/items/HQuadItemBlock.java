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
import tld.testmod.common.LocationArea;
import tld.testmod.common.blocks.HQuadBlock;

public class HQuadItemBlock extends Item implements IPlaceableBounding
{
    
    protected Block placedBlock;
    
    public HQuadItemBlock(Block blockIn)
    {
        placedBlock = blockIn;
    }
    
    @Override
    public EnumActionResult onItemUse(EntityPlayer playerIn, World worldIn, BlockPos posIn, EnumHand handIn, EnumFacing facingIn, float hitX, float hitY, float hitZ)
    {
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
            ItemStack stack = playerIn.getHeldItem(handIn);
            BlockPos posFL = posIn;

            IBlockState iblockstate = worldIn.getBlockState(posFL);
            Block block = iblockstate.getBlock();

            /** Looking at the ground or a replaceable block like grass. */
            boolean flagFL = block.isReplaceable(worldIn, posFL);
            if (!flagFL) posFL = posFL.up();

            int i = MathHelper.floor((double) (playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            EnumFacing enumfacing = EnumFacing.getHorizontal(i);
            /**get the next blocks. */
            BlockPos posFR = posFL.offset(enumfacing.rotateY());
            BlockPos posBL = posFL.offset(enumfacing);
            BlockPos posBR = posBL.offset(enumfacing.rotateY());

            if (canPlaceHere(playerIn, worldIn, stack, posIn, facingIn))
            {
                IBlockState iblockstate1 = placedBlock.getDefaultState().withProperty(HQuadBlock.FACING, enumfacing)
                        .withProperty(HQuadBlock.PART, HQuadBlock.EnumPartType.FL);

                if (worldIn.setBlockState(posFL, iblockstate1, 11))
                {
                    IBlockState iblockstate2 = iblockstate1.withProperty(HQuadBlock.PART, HQuadBlock.EnumPartType.FR);
                    if (worldIn.setBlockState(posFR, iblockstate2, 11))
                    {
                        IBlockState iblockstate3 = iblockstate2.withProperty(HQuadBlock.PART, HQuadBlock.EnumPartType.BL);
                        if (worldIn.setBlockState(posBL, iblockstate3, 11))
                        {
                            IBlockState iblockstate4 = iblockstate2.withProperty(HQuadBlock.PART, HQuadBlock.EnumPartType.BR);
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
        } 
    }


    @Override
    public boolean canPlaceHere(EntityPlayer playerIn, World worldIn, ItemStack stack, BlockPos posIn, EnumFacing facingIn)
    {
        BlockPos posFL = posIn;
        
        IBlockState iblockstate = worldIn.getBlockState(posFL);
        Block block = iblockstate.getBlock();

        /** Looking at the ground or a replaceable block like grass. */
        boolean flagFL = block.isReplaceable(worldIn, posFL);
        if (!flagFL) posFL = posFL.up();

        int i = MathHelper.floor((double) (playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EnumFacing enumfacing = EnumFacing.getHorizontal(i);
        /**get the next blocks. */
        BlockPos posFR = posFL.offset(enumfacing.rotateY());
        BlockPos posBL = posFL.offset(enumfacing);
        BlockPos posBR = posBL.offset(enumfacing.rotateY());

        if (playerIn.canPlayerEdit(posFL, facingIn, stack) && playerIn.canPlayerEdit(posFR, facingIn, stack) &&
                playerIn.canPlayerEdit(posBL, facingIn, stack) && playerIn.canPlayerEdit(posBR, facingIn, stack))
        {
            boolean flagFR = worldIn.getBlockState(posFR).getBlock().isReplaceable(worldIn, posFR);
            boolean flagBL = worldIn.getBlockState(posBL).getBlock().isReplaceable(worldIn, posBL);
            boolean flagBR = worldIn.getBlockState(posBR).getBlock().isReplaceable(worldIn, posBR);
            flagFL |= worldIn.isAirBlock(posFL);
            flagFR |= worldIn.isAirBlock(posFR);
            flagBL |= worldIn.isAirBlock(posBL);
            flagBR |= worldIn.isAirBlock(posBR);
            
            /** Disallow placing blocks on water or other unstable blocks */
            if (flagFL && flagFR && flagBL && flagBR && worldIn.getBlockState(posFL.down()).isFullyOpaque() && worldIn.getBlockState(posFR.down()).isFullyOpaque() &&
                    worldIn.getBlockState(posBL.down()).isFullyOpaque() && worldIn.getBlockState(posBR.down()).isFullyOpaque())
            {
                 return true;
            }
        }
        return false;
    }

    @Override
    public LocationArea getBoundingBox(EntityPlayer playerIn, World worldIn, BlockPos posIn)
    {
        BlockPos pos = posIn;
        int i = MathHelper.floor((double) (playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        EnumFacing enumfacing = EnumFacing.getHorizontal(i);
        
        Block block = worldIn.getBlockState(pos).getBlock();
        
        /** Looking at the ground or a replaceable block like grass. */
        if (!block.isReplaceable(worldIn, pos)) pos = pos.up();
        /** The bounding box area - horizontal 4x4 */
        return new LocationArea(pos, pos.offset(enumfacing.rotateY()).offset(enumfacing));
    }

}
