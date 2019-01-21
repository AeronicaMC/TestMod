package tld.testmod.common.animation;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.Main;

public class ForgeAnimBlock extends AnimBaseBlock
{
    
    public ForgeAnimBlock()
    {
        super(Material.WOOD);
        setCreativeTab(Main.MOD_TAB);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new ForgeAnimTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof ForgeAnimTileEntity)
            {
                ((ForgeAnimTileEntity)te).click(player.isSneaking());
            }
        }
        return true;
    }
  
}
