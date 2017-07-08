package tld.testmod.common.animation;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import tld.testmod.Main;

public class ForgeSpinBlock extends AnimBaseBlock
{

    public ForgeSpinBlock()
    {
        super(Material.WOOD);
        setCreativeTab(Main.MODTAB);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new ForgeSpinTileEntity();
    }

    /** 
     * A hack which lets you ignore the static option for models in the forge block state json
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}
