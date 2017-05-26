package tld.testmod.init;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import tld.testmod.Main;
import tld.testmod.common.animation.MyAnimBlock;
import tld.testmod.common.animation.MyAnimTileEntity;
import tld.testmod.common.animation.TestAnimBlock;
import tld.testmod.common.animation.TestAnimTileEntity;
import tld.testmod.common.blocks.HQBTest;
import tld.testmod.common.blocks.VQBTest;
import tld.testmod.common.blocks.VQBTest2;

@SuppressWarnings("unused")
public class ModBlocks
{
   
    public static final VQBTest BLOCK_VQBTEST = registerBlock(new VQBTest(), "block_vbqtest");
    public static final VQBTest2 BLOCK_VQBTEST2 = registerBlock(new VQBTest2(), "block_vbqtest2");
    public static final HQBTest BLOCK_HQBTEST = registerBlock(new HQBTest(), "block_hbqtest");
    public static final TestAnimBlock BLOCK_ANIM_TEST = registerBlock(new TestAnimBlock(), "block_anim_test");
    public static final MyAnimBlock BLOCK_MY_ANIM = registerBlock(new MyAnimBlock(), "block_my_anim");
    
    private ModBlocks() {}
    
    @Mod.EventBusSubscriber
    public static class RegistrationHandler {
        protected static final Set<Item> ITEM_BLOCKS = new HashSet<>();
        private RegistrationHandler() {}
        
        /**
         * Register this mod's {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(RegistryEvent.Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            final Block[] blocks = {
                    BLOCK_VQBTEST,
                    BLOCK_VQBTEST2,
                    BLOCK_HQBTEST,
                    BLOCK_ANIM_TEST,
                    BLOCK_MY_ANIM,
            };

            registry.registerAll(blocks);
        }

        /**
         * Register this mod's {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
            final ItemBlock[] items = {
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items) {
                registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
                ITEM_BLOCKS.add(item);
            }
        }
    }
    
    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TestAnimTileEntity.class,  Main.prependModID("tile_test_anim"));
        GameRegistry.registerTileEntity(MyAnimTileEntity.class,  Main.prependModID("tile_my_anim"));
//        GameRegistry.registerTileEntityWithAlternatives(TilePiano.class, MXTuneMain.prependModID("tile_piano"), "mxtune_tile_instrument", "PianoTile", "TileInstrument");
    }

    private static <T extends Block> T registerBlock(T block, String name) {
        block.setRegistryName(name.toLowerCase());
        block.setUnlocalizedName(block.getRegistryName().toString());
        return block;
    }

    private static <T extends Block> T registerBlock(T block) {
        return registerBlock(block, block.getClass().getSimpleName());
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String name) {
        GameRegistry.registerTileEntity(tileEntityClass, Main.prependModID(name));
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String name, String legacyName) {
        GameRegistry.registerTileEntityWithAlternatives(tileEntityClass, Main.prependModID(name), Main.prependModID(legacyName));
    }
}
