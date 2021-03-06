package tld.testmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tld.testmod.common.CommonProxy;
import tld.testmod.common.animation.*;
import tld.testmod.common.entity.EntityPull;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;
import tld.testmod.common.entity.living.EntityTimpani;
import tld.testmod.common.handlers.GUIHandler;
import tld.testmod.common.handlers.SpawnHandler;
import tld.testmod.common.storage.FileHelper;
import tld.testmod.common.storage.ServerDataManager;
import tld.testmod.common.storage.capability.MusicDBCapability;
import tld.testmod.common.world.chunk.ModChunkCapability;
import tld.testmod.init.ModBlocks;
import tld.testmod.network.PacketDispatcher;

@Mod(
        modid = Main.MOD_ID, version = Main.VERSION,
        name = Main.MOD_NAME, dependencies = Main.DEPENDENCIES,
        certificateFingerprint = "999640c365a8443393a1a21df2c0ede9488400e9"
)
public class Main
{
    public static final String MOD_ID = "testmod";
    static final String MOD_NAME = "Test Mod";
    public static final String VERSION = "{@VERSION}";
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2768,);";
    private static int modEntityId = 0;
    
    @Mod.Instance(MOD_ID)
    public static Main instance;

    @SidedProxy(clientSide = "tld.testmod.client.ClientProxy", serverSide = "tld.testmod.common.CommonProxy")
    public static CommonProxy proxy;
    
    public static final CreativeTabs MOD_TAB = new ModTab();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLogger.setLogger(event.getModLog());
        PacketDispatcher.registerPackets();
        ModChunkCapability.register();
        MusicDBCapability.register();
        proxy.preInit();
        ModBlocks.registerTileEntities();
        MinecraftForge.EVENT_BUS.register(SpawnHandler.INSTANCE);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, GUIHandler.getInstance());
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "test_skeleton"), EntityGoldenSkeleton.class, "test_skeleton", getNextModEntityId(), this, 64, 1, true, 0x000000, 0xE6BA50);
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "mob_timpani"), EntityTimpani.class, "mob_timpani", getNextModEntityId(), this, 64, 1, true, 0x000000, 0xFF5121);
        
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "entity_forge_anim_test"), ForgeAnimEntity.class, "entity_forge_anim_test", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "entity_forge_spin_test"), ForgeSpinEntity.class, "entity_forge_spin_test", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "entity_edgar_allen_block_lever"), EdgarAllenAnimEntity.class, "entity_edgar_allen_block_lever", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "entity_one_shot"), OneShotEntity.class, "entity_one_shot", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "entity_test_anim"), TestAnimEntity.class, "entity_test_anim", getNextModEntityId(), this, 64, 20, true);

        EntityRegistry.registerModEntity(new ResourceLocation(MOD_ID, "entity_pull"), EntityPull.class, "entity_pull", getNextModEntityId(), this, 64, 20, true);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit();
    }

    @Mod.EventHandler
    public void onEvent(FMLServerStartingEvent event)
    {
        FileHelper.initServerStorage();
        ServerDataManager.start();
    }

    @Mod.EventHandler
    public void onEvent(FMLServerStoppingEvent event)
    {
        ServerDataManager.shutdown();
    }

    private int getNextModEntityId()
    {
        return modEntityId++;
    }
    
    public static String prependModID(String name)
    {
        return MOD_ID + ":" + name;
    }

}
