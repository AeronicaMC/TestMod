package tld.testmod.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.common.Mod;
import tld.testmod.Main;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Function;

public class ModelMultiTex implements IModel
{
    public static final ModelResourceLocation LOCATION = new ModelResourceLocation(new ResourceLocation(Main.MOD_ID, "multitex"), "inventory");
    public static final IModel MODEL = new ModelMultiTex();

    @Nullable
    private final ResourceLocation baseLocation;

    public ModelMultiTex() { this(null); }

    public ModelMultiTex(@Nullable ResourceLocation baseLocation)
    {
        this.baseLocation = baseLocation;
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

        if (baseLocation != null)
            builder.add(baseLocation);

        return builder.build();
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        return null;
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData)
    {
        // TODO: more stuff . . .
        return new ModelMultiTex(baseLocation);
    }

    @Override
    public IModel retexture(ImmutableMap<String, String> textures)
    {
        ResourceLocation base = baseLocation;

        if (textures.containsKey("base"))
            base = new ResourceLocation(textures.get("base"));

        return new ModelMultiTex(base);
    }

    public static class MultiTexOverrideHandler extends ItemOverrideList
    {
        public static final MultiTexOverrideHandler INSTANCE = new MultiTexOverrideHandler();

        private MultiTexOverrideHandler()
        {
            super(ImmutableList.of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
        {
            return super.handleItemState(originalModel, stack, world, entity);
        }
    }
}
