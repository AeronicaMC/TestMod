package tld.testmod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tld.testmod.Main;
import tld.testmod.common.animation.OneShotEntity;

public class RenderOneShotEntity extends RenderLiving<OneShotEntity>
{
    
    private static final ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MOD_ID, "one_shot"), "entity");
    public static final IRenderFactory<OneShotEntity> FACTORY = (RenderManager manager) -> new RenderOneShotEntity(manager);
    
    @SuppressWarnings("deprecation")
    public RenderOneShotEntity(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, new net.minecraftforge.client.model.animation.AnimationModelBase<OneShotEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
        {
            @Override
            public void render(Entity entity, float limbSwing, float limbSwingSpeed, float timeAlive, float yawHead, float rotationPitch, float scale)
            {
                float time = ((short)entity.getEntityWorld().getTotalWorldTime() + Animation.getPartialTickTime());
                super.render(entity, limbSwing, limbSwingSpeed, time, yawHead, rotationPitch, scale);
            }
            @Override
            public void handleEvents(OneShotEntity te, float time, Iterable<Event> pastEvents)
            {
                te.handleEvents(time, pastEvents);
            }
        }, 0.5f);
    }
        
    public RenderOneShotEntity(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(OneShotEntity entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

}
