package tld.testmod.client.model;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import tld.testmod.Main;
import tld.testmod.ModLogger;

public class ModelLoaderMultiTex implements ICustomModelLoader
{
    public static final ModelLoaderMultiTex INSTANCE = new ModelLoaderMultiTex();
    public static String EXTENSION = ".mtex";

    private ModelLoaderMultiTex() { /* NOP */ }

    @Override
    public IModel loadModel(ResourceLocation modelLocation)
    {
        IModel model = null;
        try
        {
            model = ModelLoaderRegistry.getMissingModel();
            ModLogger.info("ModelLoaderMultiTex#loadModel %s, isEmpty=%s", modelLocation.getPath(), model.getTextures().isEmpty());
        } catch(Exception e) {
            ModLogger.error(e);
        }
        return model;
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        if (modelLocation.getNamespace().contains(Main.MOD_ID))
            ModLogger.info("ModelLoaderMultiTex#accepts %s. accept=%s", modelLocation.toString(), modelLocation.getPath().endsWith(EXTENSION));
        return modelLocation.getPath().endsWith(EXTENSION);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) { /* NOP */ }
}
