package tld.testmod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tld.testmod.Main;

public class ItemMultiTex extends Item
{
    public ItemMultiTex()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(Main.MOD_TAB);
        this.setHasSubtypes(false);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {

        return new ActionResult<ItemStack>(EnumActionResult.PASS, ItemStack.EMPTY);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
}
