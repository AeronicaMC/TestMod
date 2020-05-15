package tld.testmod.client.model;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.tuple.Pair;
import tld.testmod.Main;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

public class BakedModelMultiTex implements IBakedModel
{
    private IBakedModel baseModelMultiTex;
    private ItemOverrideListMultiTex itemOverrideListMultiTex;

    public static final ModelResourceLocation modelResourceLocation
            = new ModelResourceLocation(Main.MOD_ID + ":multitex", "inventory");

    @SuppressWarnings("unchecked")
    public BakedModelMultiTex(IBakedModel i_baseModelMultiTex)
    {
        baseModelMultiTex = i_baseModelMultiTex;
        itemOverrideListMultiTex = new ItemOverrideListMultiTex(Collections.EMPTY_LIST);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand)
    {
        return baseModelMultiTex.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion()
    {
        return baseModelMultiTex.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d()
    {
        return baseModelMultiTex.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture()
    {
        return baseModelMultiTex.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides()
    {
        return itemOverrideListMultiTex;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return baseModelMultiTex.getItemCameraTransforms();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
//    if (parentModel instanceof IPerspectiveAwareModel) {
        Matrix4f matrix4f = baseModelMultiTex.handlePerspective(cameraTransformType).getRight();
        return Pair.of(this, matrix4f);
//    } else {
//      // If the parent model isn't an IPerspectiveAware, we'll need to generate the correct matrix ourselves using the
//      //  ItemCameraTransforms.
//
//      ItemCameraTransforms itemCameraTransforms = parentModel.getItemCameraTransforms();
//      ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
//      TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
//      Matrix4f mat = null;
//      if (tr != null) { // && tr != TRSRTransformation.identity()) {
//        mat = tr.getMatrix();
//      }
//      // The TRSRTransformation for vanilla items have blockCenterToCorner() applied, however handlePerspective
//      //  reverses it back again with blockCornerToCenter().  So we don't need to apply it here.
//
//      return Pair.of(this, mat);
//    }
    }
}
