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
import tld.testmod.common.animation.MyAnimEntity;
import tld.testmod.common.animation.TestAnimEntity;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;
import tld.testmod.common.entity.living.EntityTimpani;
import tld.testmod.common.handlers.SpawnHandler;
import tld.testmod.init.ModBlocks;
import tld.testmod.init.ModSoundEvents;

@Mod(modid = Main.MODID, name = Main.MODNAME, version = Main.VERSION)
public class Main
{
    public static final String MODID = "testmod";
    public static final String MODNAME = "Test Mod";
    public static final String VERSION = "{@VERSION}";
    
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
        proxy.preInit(event);
        ModBlocks.registerTileEntities();
        MinecraftForge.EVENT_BUS.register(SpawnHandler.INSTANCE);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID,  "test_skeleton"), EntityGoldenSkeleton.class, "test_skeleton", 0, this, 64, 1, true, 0x000000, 0xE6BA50);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID,  "mob_timpani"), EntityTimpani.class, "mob_timpani", 1, this, 64, 1, true, 0x000000, 0xFF5121);
        
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_anim_test"), TestAnimEntity.class, "entity_anim_test", 2, this, 64, 20, true, 0xFFAAAA00, 0xFFDDDD00);
        EntityRegistry.registerModEntity(new ResourceLocation(MODID, "entity_my_anim"), MyAnimEntity.class, "entity_my_anim", 3, this, 64, 20, true, 0xFFAAAA00, 0xFFDDDD00);

    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }

    public static String prependModID(String name)
    {
        return MODID + ":" + name;
    }

}
