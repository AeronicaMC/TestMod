
package tld.testmod.common.items;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.client.gui.GuiGuid;
import tld.testmod.common.storage.capability.MusicDBHelper;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.client.OpenGuiMessage;

import javax.annotation.Nullable;
import java.util.List;

public class ItemGuiDBTest extends Item
{
    public ItemGuiDBTest()
    {
        this.setMaxStackSize(1);
        this.setCreativeTab(Main.MOD_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        if (!worldIn.isRemote)
        {
            MusicDBHelper.collectUserMusicData(playerIn, true);
            PacketDispatcher.sendTo(new OpenGuiMessage(GuiGuid.GUI_DB), (EntityPlayerMP) playerIn);
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean getShareTag() {return true;}

    @Override
    public int getMaxItemUseDuration(ItemStack itemstack) {return 1;}

    @Override
    public void addInformation(ItemStack stackIn, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn)
    {
        tooltip.add(TextFormatting.RESET + I18n.format("item.testmod:gui_db_test.name"));
    }
}
