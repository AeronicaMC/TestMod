package tld.testmod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tld.testmod.common.CommonProxy;
import tld.testmod.common.animation.EdgarAllenAnimEntity;
import tld.testmod.common.animation.ForgeAnimEntity;
import tld.testmod.common.animation.ForgeSpinEntity;
import tld.testmod.common.animation.OneShotEntity;
import tld.testmod.common.animation.TestAnimEntity;
import tld.testmod.common.entity.EntityPull;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;
import tld.testmod.common.entity.living.EntityTimpani;
import tld.testmod.common.handlers.SpawnHandler;
import tld.testmod.init.ModBlocks;
import tld.testmod.init.ModSoundEvents;
import tld.testmod.network.PacketDispatcher;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main
{
    public static final String MODID = "testmod";
    public static final String MODNAME = "Test Mod";
    public static final String VERSION = "{@VERSION}";
    static int modEntityId = 0;
    
    @Mod.Instance(MODID)
    public static Main instance;

    @SidedProxy(clientSide = "tld.testmod.client.ClientProxy", serverSide = "tld.testmod.common.CommonProxy")
    public static CommonProxy proxy;
    
    public static final CreativeTabs MODTAB  = new ModTab();
    public static final ModSoundEvents MSE_INSTANCE = ModSoundEvents.getInstance();
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLogger.setLogger(event.getModLog());
        PacketDispatcher.registerPackets();
        proxy.preInit(event);
        ModBlocks.registerTileEntities();
        MinecraftForge.EVENT_BUS.register(SpawnHandler.INSTANCE);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID,  "test_skeleton"), EntityGoldenSkeleton.class, "test_skeleton", getNextModEntityId(), this, 64, 1, true, 0x000000, 0xE6BA50);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID,  "mob_timpani"), EntityTimpani.class, "mob_timpani", getNextModEntityId(), this, 64, 1, true, 0x000000, 0xFF5121);
        
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_forge_anim_test"), ForgeAnimEntity.class, "entity_forge_anim_test", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_forge_spin_test"), ForgeSpinEntity.class, "entity_forge_spin_test", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_edgar_allen_block_lever"), EdgarAllenAnimEntity.class, "entity_edgar_allen_block_lever", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_one_shot"), OneShotEntity.class, "entity_one_shot", getNextModEntityId(), this, 64, 20, true);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_test_anim"), TestAnimEntity.class, "entity_test_anim", getNextModEntityId(), this, 64, 20, true);

        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_pull"), EntityPull.class, "entity_pull", getNextModEntityId(), this, 64, 20, true);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    private int getNextModEntityId()
    {
        return modEntityId++;
    }
    
    public static String prependModID(String name)
    {
        return MODID + ":" + name;
    }

}
