package tld.testmod.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.pipeline.VertexLighterSmoothAo;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tld.testmod.Main;
import tld.testmod.common.animation.ForgeSpinEntity;

public class RenderForgeSpinEntity extends RenderLiving<ForgeSpinEntity>
{
    
    private static final ResourceLocation location = new ModelResourceLocation(new ResourceLocation(Main.MOD_ID, "forge_spin_test"), "entity");
    public static final IRenderFactory<ForgeSpinEntity> FACTORY = (RenderManager manager) -> new RenderForgeSpinEntity(manager);
    
    @SuppressWarnings("deprecation")
    public RenderForgeSpinEntity(RenderManager rendermanagerIn)
    {
        super(rendermanagerIn, new net.minecraftforge.client.model.animation.AnimationModelBase<ForgeSpinEntity>(location, new VertexLighterSmoothAo(Minecraft.getMinecraft().getBlockColors()))
        {
            @Override
            public void handleEvents(ForgeSpinEntity te, float time, Iterable<Event> pastEvents)
            {
                te.handleEvents(time, pastEvents);
            }
        }, 0.5f);
    }
        
    public RenderForgeSpinEntity(RenderManager rendermanagerIn, ModelBase modelbaseIn, float shadowsizeIn)
    {
        super(rendermanagerIn, modelbaseIn, shadowsizeIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(ForgeSpinEntity entity)
    {
        return TextureMap.LOCATION_BLOCKS_TEXTURE;
    }

}
