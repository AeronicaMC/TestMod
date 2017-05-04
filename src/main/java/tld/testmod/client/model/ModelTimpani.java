package tld.testmod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMagmaCube;
import tld.testmod.common.entity.living.EntityTimpani;

/**
 * mxmob_timpani - aeronica
 * Created using Tabula 5.1.0
 */
public class ModelTimpani extends ModelBase {
    public ModelRenderer kettle;
    public ModelRenderer head;
    public ModelRenderer base_rear;
    public ModelRenderer base_front;
    public ModelRenderer base_right;
    public ModelRenderer base_left;

    public ModelTimpani() {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.base_rear = new ModelRenderer(this, 36, 0);
        this.base_rear.setRotationPoint(0.0F, 9.0F, 0.0F);
        this.base_rear.addBox(-3.0F, 0.0F, 3.0F, 6, 1, 1, 0.0F);
        this.kettle = new ModelRenderer(this, 0, 0);
        this.kettle.setRotationPoint(0.0F, 12.0F, 0.0F);
        this.kettle.addBox(-6.0F, -4.0F, -6.0F, 12, 8, 12, 0.0F);
        this.head = new ModelRenderer(this, 0, 20);
        this.head.setRotationPoint(0.0F, -5.0F, 0.0F);
        this.head.addBox(-6.5F, -1.0F, -6.5F, 13, 2, 13, 0.0F);
        this.base_right = new ModelRenderer(this, 36, 4);
        this.base_right.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base_right.addBox(-4.0F, 0.0F, -4.0F, 8, 1, 1, 0.0F);
        this.setRotateAngle(base_right, 0.0F, 1.5707963267948966F, 0.0F);
        this.base_left = new ModelRenderer(this, 36, 6);
        this.base_left.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base_left.addBox(-4.0F, 0.0F, 3.0F, 8, 1, 1, 0.0F);
        this.setRotateAngle(base_left, 0.0F, 1.5707963267948966F, 0.0F);
        this.base_front = new ModelRenderer(this, 36, 2);
        this.base_front.setRotationPoint(0.0F, 0.0F, 0.0F);
        this.base_front.addBox(-3.0F, 0.0F, -4.0F, 6, 1, 1, 0.0F);
        this.kettle.addChild(this.base_rear);
        this.kettle.addChild(this.head);
        this.base_rear.addChild(this.base_right);
        this.base_rear.addChild(this.base_left);
        this.base_rear.addChild(this.base_front);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) { 
        this.kettle.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
    
    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    @Override
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTickTime)
    {
        EntityTimpani entityTimpani = (EntityTimpani)entitylivingbaseIn;
        float f = entityTimpani.prevSquishFactor + (entityTimpani.squishFactor - entityTimpani.prevSquishFactor) * partialTickTime;
        head.offsetY =  -f * 0.25F;
        base_rear.offsetY = f * 0.5F;
        super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
    }
}
