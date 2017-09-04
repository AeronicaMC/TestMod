package tld.testmod.common.animation;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.common.items.ItemPull;
import tld.testmod.common.items.ItemTuningFork;
import tld.testmod.init.ModSoundEvents;

public class OneShotBlock extends AnimBaseBlock
{

    public OneShotBlock()
    {
        super(Material.WOOD);
        setCreativeTab(Main.MODTAB);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new OneShotTileEntity();
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        /*
         * Pull Rope - Do Nothing
         * Tuning Fork - Increment Note
         */
        if(!world.isRemote)
        {
            TileEntity te = world.getTileEntity(pos);
            if(te instanceof OneShotTileEntity && !isRope(player, hand))
            {
                ((OneShotTileEntity)te).click(isTuningFork(player, hand));
            }
        }
        return !isRope(player, hand);
    }
    
    private boolean isRope(EntityPlayer player, EnumHand hand)
    {
        return (player.getHeldItem(hand).getItem() instanceof ItemPull);
    }
    
    private boolean isTuningFork(EntityPlayer player, EnumHand hand)
    {
        return (player.getHeldItem(hand).getItem() instanceof ItemTuningFork);
    }
    
    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int id, int param)
    {
        if(worldIn.isRemote)
        {
            TileEntity te = worldIn.getTileEntity(pos);
            if(te instanceof OneShotTileEntity)
            {
                ((OneShotTileEntity)te).triggerOneShot();
            }
        }

        float f = (float)Math.pow(2.0D, (double)(param - 12) / 12.0D);
        worldIn.playSound(null, pos, ModSoundEvents.BELL,  SoundCategory.RECORDS, 3.0F, f );
        worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.2D, (double)pos.getZ() + 0.5D, (double)param / 24.0D, 0.0D, 0.0D, new int[0]);
        ModLogger.info("eventReceived: %d", param);
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

        if (te instanceof OneShotTileEntity)
        {
            OneShotTileEntity oneShotTE = (OneShotTileEntity)te;
            if (oneShotTE.isPreviousRedstoneState() != flag)
            {
                if (flag)
                    oneShotTE.triggerOneShot();

                oneShotTE.setPreviousRedstoneState(flag);
            }
        }
    }

 }
