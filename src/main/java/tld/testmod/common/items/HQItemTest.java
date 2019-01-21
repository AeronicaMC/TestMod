package tld.testmod.common.items;

import net.minecraft.block.Block;
import tld.testmod.Main;

public class HQItemTest extends HQuadItemBlock
{

    public HQItemTest(Block blockIn)
    {
        super(blockIn);
        setMaxStackSize(1);
        setCreativeTab(Main.MOD_TAB);
    }

}
