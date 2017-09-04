package tld.testmod.client;

import com.google.common.collect.ImmutableMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.animation.ITimeValue;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.Main;
import tld.testmod.client.render.RenderGoldenSkeleton;
import tld.testmod.client.render.RenderPull;
import tld.testmod.client.render.RenderTimpani;
import tld.testmod.common.CommonProxy;
import tld.testmod.common.animation.EdgarAllenAnimEntity;
import tld.testmod.common.animation.ForgeAnimEntity;
import tld.testmod.common.animation.ForgeSpinEntity;
import tld.testmod.common.animation.ModAnimation;
import tld.testmod.common.animation.OneShotEntity;
import tld.testmod.common.animation.TestAnimEntity;
import tld.testmod.common.entity.EntityPull;
import tld.testmod.common.entity.EntityTimpaniFx;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;
import tld.testmod.common.entity.living.EntityTimpani;
import tld.testmod.init.ModItems;

public class ClientProxy extends CommonProxy
{

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldenSkeleton.class, RenderGoldenSkeleton.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityTimpani.class, RenderTimpani.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityPull.class, RenderPull.FACTORY);
        
        RenderingRegistry.registerEntityRenderingHandler(ForgeAnimEntity.class, new IRenderFactory<ForgeAnimEntity>()
        {
            @SuppressWarnings("deprecation")
            public Render<ForgeAnimEntity> createRenderFor(RenderManager manager)
            {
                ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MODID, "forge_anim_test"), "entity");
                return new RenderLiving<ForgeAnimEntity>(manager, new net.minecraftforge.client.model.animation.AnimationModelBase<ForgeAnimEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
                    {
                        @Override
                        public void handleEvents(ForgeAnimEntity te, float time, Iterable<Event> pastEvents)
                        {
                            te.handleEvents(time, pastEvents);
                        }
                    }, 0.5f)
                {
                    protected ResourceLocation getEntityTexture(ForgeAnimEntity entity)
                    {
                        return TextureMap.LOCATION_BLOCKS_TEXTURE;
                    }
                };
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(ForgeSpinEntity.class, new IRenderFactory<ForgeSpinEntity>()
        {
            @SuppressWarnings("deprecation")
            public Render<ForgeSpinEntity> createRenderFor(RenderManager manager)
            {
                ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MODID, "forge_spin_test"), "entity");
                return new RenderLiving<ForgeSpinEntity>(manager, new net.minecraftforge.client.model.animation.AnimationModelBase<ForgeSpinEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
                    {
                        @Override
                        public void handleEvents(ForgeSpinEntity te, float time, Iterable<Event> pastEvents)
                        {
                            te.handleEvents(time, pastEvents);
                        }
                    }, 0.5f)
                {
                    protected ResourceLocation getEntityTexture(ForgeSpinEntity entity)
                    {
                        return TextureMap.LOCATION_BLOCKS_TEXTURE;
                    }
                };
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(EdgarAllenAnimEntity.class, new IRenderFactory<EdgarAllenAnimEntity>()
        {
            @SuppressWarnings("deprecation")
            public Render<EdgarAllenAnimEntity> createRenderFor(RenderManager manager)
            {
                ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MODID, "edgar_allen_block_lever"), "entity");
                return new RenderLiving<EdgarAllenAnimEntity>(manager, new net.minecraftforge.client.model.animation.AnimationModelBase<EdgarAllenAnimEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
                    {
                        @Override
                        public void handleEvents(EdgarAllenAnimEntity te, float time, Iterable<Event> pastEvents)
                        {
                            te.handleEvents(time, pastEvents);
                        }
                    }, 0.5f)
                {
                    protected ResourceLocation getEntityTexture(EdgarAllenAnimEntity entity)
                    {
                        return TextureMap.LOCATION_BLOCKS_TEXTURE;
                    }
                };
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(OneShotEntity.class, new IRenderFactory<OneShotEntity>()
        {
            @SuppressWarnings("deprecation")
            public Render<OneShotEntity> createRenderFor(RenderManager manager)
            {
                ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MODID, "one_shot"), "entity");
                return new RenderLiving<OneShotEntity>(manager, new net.minecraftforge.client.model.animation.AnimationModelBase<OneShotEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
                {
                    @Override
                    public void render(Entity entity, float limbSwing, float limbSwingSpeed, float timeAlive, float yawHead, float rotationPitch, float scale)
                    {
                        float time = ((short)entity.getEntityWorld().getTotalWorldTime() + ModAnimation.getPartialTickTime());
                        super.render(entity, limbSwing, limbSwingSpeed, time, yawHead, rotationPitch, scale);
                    }
                    @Override
                    public void handleEvents(OneShotEntity te, float time, Iterable<Event> pastEvents)
                    {
                        te.handleEvents(time, pastEvents);
                    }
                }, 0.5f)
                {
                    protected ResourceLocation getEntityTexture(OneShotEntity entity)
                    {
                        return TextureMap.LOCATION_BLOCKS_TEXTURE;
                    }
                };
            }
        });
        RenderingRegistry.registerEntityRenderingHandler(TestAnimEntity.class, new IRenderFactory<TestAnimEntity>()
        {
            @SuppressWarnings("deprecation")
            public Render<TestAnimEntity> createRenderFor(RenderManager manager)
            {
                ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MODID, "test_anim"), "entity");
                return new RenderLiving<TestAnimEntity>(manager, new net.minecraftforge.client.model.animation.AnimationModelBase<TestAnimEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
                    {
                        @Override
                        public void handleEvents(TestAnimEntity te, float time, Iterable<Event> pastEvents)
                        {
                            te.handleEvents(time, pastEvents);
                        }
                    }, 0.5f)
                {
                    protected ResourceLocation getEntityTexture(TestAnimEntity entity)
                    {
                        return TextureMap.LOCATION_BLOCKS_TEXTURE;
                    }
                };
            }
        });

    }
    
    @Override
    public void spawnTimpaniParticle(World world, double x, double y, double z) {
      Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTimpaniFx(world, x, y, z, Items.BREAD, Items.BREAD.getMetadata(0)));
    }

    @Override
    public void spawnRopeParticle(World world, double x, double y, double z)
    {
        Minecraft.getMinecraft().effectRenderer.addEffect(new EntityTimpaniFx(world, x, y, z, ModItems.ITEM_PULL, ModItems.ITEM_PULL.getMetadata(0)));
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);       
    }

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);       
    }
    
    @Override
    public Side getPhysicalSide() {return Side.CLIENT;}

    @Override
    public Side getEffectiveSide() {return FMLCommonHandler.instance().getEffectiveSide();}

    @Override
    public Minecraft getMinecraft() {return Minecraft.getMinecraft();}

    @Override
    public EntityPlayer getClientPlayer() {return Minecraft.getMinecraft().player;}

    @Override
    public World getClientWorld() {return Minecraft.getMinecraft().world;}

    @Override
    public World getWorldByDimensionId(int dimension)
    {
        Side effectiveSide = FMLCommonHandler.instance().getEffectiveSide();
        if (effectiveSide == Side.SERVER)
        {
            return FMLClientHandler.instance().getServer().getWorld(dimension);
        } else
        {
            return getClientWorld();
        }
    }
    
    @Override
    public boolean playerIsInCreativeMode(EntityPlayer player)
    {
        if (player instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            return entityPlayerMP.isCreative();
        } else if (player instanceof EntityPlayerSP)
        {
            return Minecraft.getMinecraft().playerController.isInCreativeMode();
        }
        return false;
    }

    @Override
    public IThreadListener getThreadFromContext(MessageContext ctx)
    {
        return (ctx.side.isClient() ? this.getMinecraft() : super.getThreadFromContext(ctx));
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx)
    {
        // Note that if you simply return 'Minecraft.getMinecraft().thePlayer',
        // your packets will not work as expected because you will be getting a
        // client player even when you are on the server!
        // Sounds absurd, but it's true.

        // Solution is to double-check side before returning the player:
        return (ctx.side.isClient() ? this.getClientPlayer() : super.getPlayerEntity(ctx));
    }
    
    @Override
    public IAnimationStateMachine load(ResourceLocation location, ImmutableMap<String, ITimeValue> parameters)
    {
        return ModelLoaderRegistry.loadASM(location, parameters);
    }

}