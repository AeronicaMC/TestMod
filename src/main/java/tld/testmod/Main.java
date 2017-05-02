package tld.testmod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import tld.testmod.common.CommonProxy;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;

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
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        ModLogger.setLogger(event.getModLog());
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
        EntityRegistry.registerModEntity(EntityGoldenSkeleton.class, prependModID("test_skeleton"), 0, this, 64, 1, true, 0x000000, 0xE6BA50);
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
