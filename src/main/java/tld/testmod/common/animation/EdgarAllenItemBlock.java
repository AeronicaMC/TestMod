package tld.testmod.common.animation;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import tld.testmod.Main;

/*
 * https://github.com/EdgarAllen/ForgeAnimationSystemTests
 */
public class EdgarAllenItemBlock extends ItemBlock
{

    public EdgarAllenItemBlock(Block block)
    {
        super(block);
        setHasSubtypes(false);
        setCreativeTab(Main.MOD_TAB);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new EdgarAllenItemAnimationHolder();
    }
}
