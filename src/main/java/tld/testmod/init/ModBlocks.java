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
import net.minecraftforge.registries.IForgeRegistry;
import tld.testmod.Main;
import tld.testmod.common.animation.EdgarAllenBlockLever;
import tld.testmod.common.animation.EdgarAllenTileEntity;
import tld.testmod.common.animation.ForgeAnimBlock;
import tld.testmod.common.animation.ForgeAnimTileEntity;
import tld.testmod.common.animation.ForgeSpinBlock;
import tld.testmod.common.animation.ForgeSpinTileEntity;
import tld.testmod.common.animation.OneShotBlock;
import tld.testmod.common.animation.OneShotTileEntity;
import tld.testmod.common.animation.TestAnimBlock;
import tld.testmod.common.animation.TestAnimTileEntity;
import tld.testmod.common.blocks.*;

@SuppressWarnings("unused")
public class ModBlocks
{
   
    public static final VQBTest BLOCK_VQBTEST = registerBlock(new VQBTest(), "block_vbqtest");
    public static final VQBTest2 BLOCK_VQBTEST2 = registerBlock(new VQBTest2(), "block_vbqtest2");
    public static final HQBTest BLOCK_HQBTEST = registerBlock(new HQBTest(), "block_hbqtest");
    public static final ForgeAnimBlock FORGE_ANIM_TEST = registerBlock(new ForgeAnimBlock(), "forge_anim_test");
    public static final ForgeSpinBlock FORGE_SPIN_TEST = registerBlock(new ForgeSpinBlock(), "forge_spin_test");
    public static final EdgarAllenBlockLever EDGAR_ALLEN_BLOCK_LEVER = registerBlock(new EdgarAllenBlockLever(), "edgar_allen_block_lever");
    public static final OneShotBlock ONE_SHOT = registerBlock(new OneShotBlock(), "one_shot");
    public static final TestAnimBlock TEST_ANIM = registerBlock(new TestAnimBlock(), "test_anim");
    public static final BlockPull PULL_ROPE = registerBlock(new BlockPull(true), "pull_rope");
    public static final ItemBlock ITEM_PULL_ROPE = new ItemBlock(PULL_ROPE);
    public static final BlockCarillon CARILLON = registerBlock(new BlockCarillon(), "carillon");
    public static final ItemBlock ITEM_CARILLON = new ItemBlock(CARILLON);
    public static final BlockEmissive BLOCK_EMISSIVE = registerBlock(new BlockEmissive(), "block_emissive");
    public static final ItemBlock ITEM_EMISSIVE = new ItemBlock(BLOCK_EMISSIVE);
    
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
                    FORGE_ANIM_TEST,
                    FORGE_SPIN_TEST,
                    EDGAR_ALLEN_BLOCK_LEVER,
                    ONE_SHOT,
                    TEST_ANIM,
                    PULL_ROPE,
                    CARILLON,
                    BLOCK_EMISSIVE,
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
                    ITEM_PULL_ROPE,
                    ITEM_CARILLON,
                    ITEM_EMISSIVE,
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items) {
                registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
                ITEM_BLOCKS.add(item);
            }
        }
    }
    
    public static void registerTileEntities() {
        registerTileEntity(ForgeAnimTileEntity.class, "tile_forge_anim_test");
        registerTileEntity(ForgeSpinTileEntity.class, "tile_forge_spin_test");
        registerTileEntity(EdgarAllenTileEntity.class, "tile_edgar_allen_block_lever");
        registerTileEntity(OneShotTileEntity.class, "tile_one_shot");
        registerTileEntity(TestAnimTileEntity.class, "tile_test_anim");
        registerTileEntity(CarillionTileEntity.class, "tile_carillon");
    }

    private static <T extends Block> T registerBlock(T block, String name) {
        block.setRegistryName(name.toLowerCase());
        block.setTranslationKey(block.getRegistryName().toString());
        return block;
    }

    private static <T extends Block> T registerBlock(T block) {
        return registerBlock(block, block.getClass().getSimpleName());
    }

    private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String name) {
        GameRegistry.registerTileEntity(tileEntityClass, Main.prependModID(name));
    }

}
