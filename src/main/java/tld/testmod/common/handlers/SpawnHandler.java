package tld.testmod.common.handlers;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import tld.testmod.ModLogger;
import tld.testmod.common.entity.living.EntityGoldenSkeleton;

import java.util.UUID;

import static tld.testmod.common.storage.ServerDataManager.upsertUser;

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

    @SubscribeEvent
    public void onEvent(PlayerEvent.PlayerLoggedInEvent event)
    {
        if (!event.player.getEntityWorld().isRemote)
        {
            // Create or update player in database
            upsertUser(event.player.getPersistentID(), event.player.getDisplayNameString());
            ModLogger.info("***** H2: PlayerLoggedInEvent %s, %s", event.player.getDisplayNameString(), event.player.getPersistentID());
        }
    }
}
