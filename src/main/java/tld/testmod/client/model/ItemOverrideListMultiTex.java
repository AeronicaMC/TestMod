package tld.testmod.client.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemOverrideListMultiTex extends ItemOverrideList
{
    public ItemOverrideListMultiTex(List<ItemOverride> overridesIn)
    {
        super(overridesIn);
    }

    @Override
    public IBakedModel handleItemState(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable EntityLivingBase entity)
    {
        int damage;
        if (!stack.isEmpty())
        {
            damage = stack.getItem().getDamage(stack);
        }
        return super.handleItemState(originalModel, stack, world, entity);
    }
}
