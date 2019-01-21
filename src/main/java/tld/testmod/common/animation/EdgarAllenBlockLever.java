package tld.testmod.common.animation;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tld.testmod.Main;

/*
 * https://github.com/EdgarAllen/ForgeAnimationSystemTests
 */
public class EdgarAllenBlockLever extends AnimBaseBlock
{

    private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D);
    
    public EdgarAllenBlockLever()
    {
        super(Material.WOOD);
        setCreativeTab(Main.MOD_TAB);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new EdgarAllenTileEntity();
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BASE_AABB;
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof EdgarAllenTileEntity)
            {
                ((EdgarAllenTileEntity)te).click();
            }
        }
        return true;
    }

}
