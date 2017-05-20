package tld.testmod.common;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import tld.testmod.common.items.HQItemTest;
import tld.testmod.common.items.ItemRightClickTest;
import tld.testmod.common.items.VQItemTest;
import tld.testmod.common.items.VQItemTest2;

@SuppressWarnings("unused")
public class ModItems
{
    
//    public static final ItemInstrument ITEM_INSTRUMENT = registerItem(new ItemInstrument(), "item_inst");
//    public static final ItemMusicPaper ITEM_MUSIC_PAPER = registerItem(new ItemMusicPaper(), "item_musicpaper");
    public static final ItemRightClickTest ITEM_RC_TEST = registerItem(new ItemRightClickTest(), "item_rc_test");
    public static final HQItemTest ITEM_HBQTEST = registerItem(new HQItemTest(ModBlocks.BLOCK_HQBTEST), "block_hbqtest");
    public static final VQItemTest ITEM_VBQTEST = registerItem(new VQItemTest(ModBlocks.BLOCK_VQBTEST), "block_vbqtest");
    public static final VQItemTest2 ITEM_VBQTEST2 = registerItem(new VQItemTest2(ModBlocks.BLOCK_VQBTEST2), "block_vbqtest2");
   
    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        protected static final Set<Item> ITEMS = new HashSet<>();

        /**
         * Register this mod's {@link Item}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItems(RegistryEvent.Register<Item> event) {
            final Item[] items = {
//                    ITEM_INSTRUMENT,
//                    ITEM_MUSIC_PAPER,
//                    ITEM_SHEET_MUSIC,
//                    ITEM_CONVERTER,
                    ITEM_RC_TEST,
                    ITEM_VBQTEST,
                    ITEM_VBQTEST2,
                    ITEM_HBQTEST,
            };

            final IForgeRegistry<Item> registry = event.getRegistry();
            for (final Item item : items) {
                registry.register(item);
                ITEMS.add(item);
            }
        }
    }
        
    private static <T extends Item> T registerItem(T item, String name) {
        item.setRegistryName(name.toLowerCase());
        item.setUnlocalizedName(item.getRegistryName().toString());
        return item;
    }

    private static <T extends Item> T registerItem(T item) {
        String simpleName = item.getClass().getSimpleName();
        if (item instanceof ItemBlock) {
            simpleName = ((ItemBlock) item).getBlock().getClass().getSimpleName();
        }
        return registerItem(item, simpleName);
    }

}
