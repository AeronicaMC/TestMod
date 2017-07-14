package tld.testmod.common.animation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.Main;

public class TestAnimBlock extends AnimBaseBlock
{

    public TestAnimBlock()
    {
        super(Material.WOOD);
        this.setCreativeTab(Main.MODTAB);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TestAnimTileEntity();
    }
    
    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if(!world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof TestAnimTileEntity)
            {
                ((TestAnimTileEntity)te).click(false);
            }
        }
        return true;
    }

    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        if(worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof TestAnimTileEntity)
            {
                ((TestAnimTileEntity)te).click(param==1 ? true: false);
            }
        }

        if (param == 1)
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_IRON_TRAPDOOR_OPEN,  SoundCategory.BLOCKS, 0.5F, 1.0F );
            worldIn.spawnParticle(EnumParticleTypes.HEART, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.2D, (double)pos.getZ() + 0.5D, (double)param / 24.0D, 0.0D, 0.0D, new int[0]);
        }
        else
        {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_IRON_TRAPDOOR_CLOSE,  SoundCategory.BLOCKS, 0.5F, 1.0F );
        }
        return true;
    }
    
    /**
     * React to a redstone powered neighbor block
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        boolean flag = worldIn.isBlockPowered(pos);
        TileEntity te = worldIn.getTileEntity(pos);

        if (te instanceof TestAnimTileEntity)
        {
            TestAnimTileEntity testAnimTE = (TestAnimTileEntity)te;
            if (testAnimTE.isPreviousRedstoneState() != flag)
            {
                if (flag)
                    testAnimTE.click(false);

                testAnimTE.setPreviousRedstoneState(flag);
            }
        }
    }

}
