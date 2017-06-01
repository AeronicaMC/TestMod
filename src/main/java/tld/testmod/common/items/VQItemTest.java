package tld.testmod.common.items;


import net.minecraft.block.Block;
import tld.testmod.Main;

public class VQItemTest extends VQuadItemBlock
{

    public VQItemTest(Block blockIn)
    {
        super(blockIn);
        setMaxStackSize(1);
        setCreativeTab(Main.MODTAB);
    }

}
