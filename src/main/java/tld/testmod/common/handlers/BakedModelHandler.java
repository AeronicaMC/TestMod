package tld.testmod.common.handlers;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tld.testmod.client.model.BakedModelMultiTex;

public class BakedModelHandler
{
    public static final BakedModelHandler INSTANCE = new BakedModelHandler();
    private BakedModelHandler() {}

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event)
    {
        Object object =  event.getModelRegistry().getObject(BakedModelMultiTex.modelResourceLocation);
        if (object != null)
        {
            IBakedModel existingModel = (IBakedModel) object;
            BakedModelMultiTex customModel = new BakedModelMultiTex(existingModel);
            event.getModelRegistry().putObject(BakedModelMultiTex.modelResourceLocation, customModel);
        }
    }
}
