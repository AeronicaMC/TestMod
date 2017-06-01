package tld.testmod.common.handlers;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tld.testmod.ModLogger;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;

public enum SpawnHandler
{
   
    INSTANCE;

    @SuppressWarnings({
            "rawtypes", "unchecked"
    })
    @SubscribeEvent
    public void onEvent(EntityJoinWorldEvent event)
    {   
       if((event.getEntity() instanceof EntityWolf) && (event.getEntity() instanceof EntityLiving))
       {
            ModLogger.info("%s Spawns side: %s",event.getEntity() ,!event.getWorld().isRemote ? "Server" : "Client");
            // Add a task so wolves will attack the Golden Skeletons
            ((EntityLiving) event.getEntity()).targetTasks.addTask(6, new EntityAINearestAttackableTarget((EntityWolf) event.getEntity(), EntityGoldenSkeleton.class, false));
       }
    }

}
