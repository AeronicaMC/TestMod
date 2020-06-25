package tld.testmod.client.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ItemLayerModel;
import tld.testmod.Main;
import tld.testmod.ModLogger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

@SuppressWarnings("deprecation")
public class ModelLoaderMultiTex implements ICustomModelLoader, JsonDeserializationContext
{
    public static final ModelLoaderMultiTex INSTANCE = new ModelLoaderMultiTex();
    public static String EXTENSION = ".mtex";

    private final Gson gson = new GsonBuilder().registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3fDeserializer()).registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransformsDeserializer()).create();
    private final JsonParser parser = new JsonParser();
    private final ModelBlock.Deserializer modelBlockDeserializer = new ModelBlock.Deserializer();
    private IResourceManager manager;
    private List<ItemOverride> overrides;

    private ModelLoaderMultiTex() { /* NOP */ }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws IOException
    {
        String modelPath = modelLocation.getPath();
        modelPath = modelPath.substring(0, modelPath.lastIndexOf('.')) + ".mtex.json";
        IResource resource = this.manager.getResource(new ResourceLocation(modelLocation.getNamespace(), modelPath));
        InputStreamReader jsonStream = new InputStreamReader(resource.getInputStream());
        JsonElement json = this.parser.parse(jsonStream);
        jsonStream.close();
        ModelBlock modelBlock = this.modelBlockDeserializer.deserialize(json, ModelBlock.class, this);
        ImmutableList.Builder<ResourceLocation> builder = ImmutableList.builder();
        int layer = 0;
        String texture;
        while ((texture = modelBlock.textures.get("layer" + layer++)) != null) {
            builder.add(new ResourceLocation(texture));
        }
        //return new ItemLayerModel(modelBlock);
        return new ModelMultiTex(modelBlock);
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation)
    {
        if (modelLocation.getNamespace().contains(Main.MOD_ID))
            ModLogger.info("ModelLoaderMultiTex#accepts %s. accept=%s", modelLocation.toString(), modelLocation.getPath().endsWith(EXTENSION));
        return modelLocation.getNamespace().contains(Main.MOD_ID) && modelLocation.getPath().endsWith(EXTENSION);
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager)
    {
        manager = resourceManager;
    }

    @Override
    public <T> T deserialize(JsonElement json, Type type) throws JsonParseException
    {
        return this.gson.fromJson(json, type);
    }
}
