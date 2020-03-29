package tld.testmod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.library.client.midi.IActiveNoteReceiver;
import tld.testmod.library.client.midi.MidiUtils;

public class ItemRightClickTest extends Item implements IActiveNoteReceiver
{

    public ItemRightClickTest()
    {
        setMaxStackSize(1);
        setHasSubtypes(false);
        setCreativeTab(Main.MOD_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        ModLogger.info("Right Clicked");
        MidiUtils.INSTANCE.setNoteReceiver(this, worldIn, playerIn, handIn, itemstack);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    @Override
    public void noteReceiver(World worldIn, BlockPos posIn, int entityID, byte noteIn, byte volumeIn)
    {
        if (!worldIn.isRemote && volumeIn != 0)
        {
            EntityPlayer player = (EntityPlayer) worldIn.getEntityByID(entityID);
            BlockPos pos = player.getPosition();
            byte pitch = (byte) (noteIn - 48);
            float f = (float)Math.pow(2.0D, (double)(pitch - 12) / 12.0D);
            worldIn.playSound((EntityPlayer) null, player.getPosition(), SoundEvents.BLOCK_NOTE_HARP, SoundCategory.RECORDS, 3.0F, f);
            // spawnParticle does nothing server side. A special packet is needed to do this on the client side.
            worldIn.spawnParticle(EnumParticleTypes.NOTE, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.5D, (double)pos.getZ() + 0.5D, (double)pitch / 24.0D, 0.0D, 0.0D, new int[0]);
        }
        
    }
    
}
