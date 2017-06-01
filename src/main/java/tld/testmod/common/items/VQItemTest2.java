package tld.testmod.common.items;

import net.minecraft.block.Block;
import tld.testmod.Main;

public class VQItemTest2 extends VQuadItemBlock
{

    public VQItemTest2(Block blockIn)
    {
        super(blockIn);
        setMaxStackSize(1);
        setCreativeTab(Main.MODTAB);
    }

}
