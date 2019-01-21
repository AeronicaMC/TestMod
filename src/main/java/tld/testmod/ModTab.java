package tld.testmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModTab extends CreativeTabs
{

    public ModTab()
    {
        super(Main.MOD_ID);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTranslationKey()
    {
        return Main.MOD_NAME;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon()
    {
        return new ItemStack(Items.APPLE);
    }
}
