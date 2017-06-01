package tld.testmod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.common.LocationArea;

public interface IPlaceableBounding
{

    public boolean canPlaceHere(EntityPlayer playerIn, World worldIn, ItemStack stack, BlockPos posIn, EnumFacing facingIn);
    
    public LocationArea getBoundingBox(EntityPlayer playerIn, World worldIn, BlockPos posIn);
    
}
