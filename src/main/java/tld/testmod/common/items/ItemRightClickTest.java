package tld.testmod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.ModLogger;

public class ItemRightClickTest extends Item
{

    public ItemRightClickTest()
    {
        setMaxStackSize(1);
        setHasSubtypes(false);
        setCreativeTab(Main.MODTAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        ModLogger.info("Right Clicked");
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }
    
}
