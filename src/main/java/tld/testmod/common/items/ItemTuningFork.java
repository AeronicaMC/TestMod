package tld.testmod.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.common.world.ModGlobalWorldSaveData;
import tld.testmod.common.world.chunk.IModChunkData;
import tld.testmod.common.world.chunk.ModChunkDataHelper;

public class ItemTuningFork extends Item
{

    public ItemTuningFork()
    {
        this.maxStackSize = 1;
        this.setCreativeTab(Main.MOD_TAB);
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        playerIn.setActiveHand(handIn);
        ModLogger.info("Right Clicked");
        testModGlobalWorldSaveData(worldIn, playerIn);
        testModChunkData(worldIn, playerIn);

        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack)
    {
        return 72000;
    }

    private void testModGlobalWorldSaveData(World world, EntityPlayer entityPlayer)
    {
        ModGlobalWorldSaveData modGlobalWorldSaveData = ModGlobalWorldSaveData.get(world);
        if (!world.isRemote && modGlobalWorldSaveData != null)
        {
            ModLogger.info("Mod Global World Save Data: name = %s, number = %s, isSomething = %s",
                           modGlobalWorldSaveData.getName(),
                           modGlobalWorldSaveData.getNumber(),
                           modGlobalWorldSaveData.isSomething());
            modGlobalWorldSaveData.setSomething(!modGlobalWorldSaveData.isSomething());
            modGlobalWorldSaveData.setName(entityPlayer.getDisplayNameString());
            modGlobalWorldSaveData.setNumber(entityPlayer.getEntityId());
        }
    }

    private void testModChunkData(World world, EntityPlayer entityPlayer)
    {
        if (!world.isRemote)
        {
            Chunk chunk = world.getChunk(entityPlayer.getPosition());
            boolean isFunctional = ModChunkDataHelper.isFunctional(chunk);
            String string = ModChunkDataHelper.getString(chunk);
            ModLogger.info("Mod Chunk Data (%s): isFunctional = %s, by = %s", chunk.getPos(), isFunctional, string);
            ModChunkDataHelper.setFunctional(chunk, !isFunctional);
            ModChunkDataHelper.setString(chunk, entityPlayer.getDisplayNameString());
        }
    }
}
