package tld.testmod.common.animation;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import tld.testmod.Main;

public class ForgeSpinItemBlock extends ItemBlock
{

    public ForgeSpinItemBlock(Block block)
    {
        super(block);
        setHasSubtypes(false);
        setCreativeTab(Main.MOD_TAB);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt)
    {
        return new ForgeItemSpinHolder();
    }
}
