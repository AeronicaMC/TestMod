package tld.testmod.common.entity.living;


import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityTestSkeleton extends EntitySkeleton
{
    
    public EntityTestSkeleton(World worldIn)
    {
        super(worldIn);
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        this.setArrowCountInEntity(256);   
    }
    
    @Override
    protected boolean canDespawn() {
        return false;
    }
    
}