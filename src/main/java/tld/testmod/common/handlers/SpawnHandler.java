/**
 * Copyright {2016} Paul Boese aka Aeronica
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
