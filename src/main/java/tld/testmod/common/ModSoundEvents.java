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
package tld.testmod.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tld.testmod.Main;

public class ModSoundEvents {
    public static final SoundEvent ENTITY_TINY_TIMPANI_SQUISH;
    public static final SoundEvent ENTITY_MEDIUM_TIMPANI_SQUISH;
    public static final SoundEvent ENTITY_LARGE_TIMPANI_SQUISH;
    public static final SoundEvent ENTITY_TIMPANI_JUMP;
    public static final SoundEvent ENTITY_TINY_TIMPANI_HURT;
    public static final SoundEvent ENTITY_TINY_TIMPANI_DEATH;
    public static final SoundEvent ENTITY_TIMPANI_HURT;
    public static final SoundEvent ENTITY_TIMPANI_DEATH;
    /**
     * Register the {@link SoundEvent}s.
     */
    private ModSoundEvents() {}
    
    static {
        ENTITY_TINY_TIMPANI_SQUISH = registerSound("entity.tiny.timpani.squish");
        ENTITY_MEDIUM_TIMPANI_SQUISH = registerSound("entity.medium.timpani.squish");
        ENTITY_LARGE_TIMPANI_SQUISH = registerSound("entity.large.timpani.squish");
        ENTITY_TIMPANI_JUMP = registerSound("entity.timpani.jump");
        ENTITY_TINY_TIMPANI_HURT = registerSound("entity.small.timpani.hurt");
        ENTITY_TINY_TIMPANI_DEATH = registerSound("entity.small.timpani.death");    
        ENTITY_TIMPANI_HURT = registerSound("entity.timpani.hurt");
        ENTITY_TIMPANI_DEATH = registerSound("entity.timpani.death");
    }
    /**
     * Register a {@link SoundEvent}.
     * 
     * @author Choonster
     * @param soundName The SoundEvent's name without the [MODID] prefix
     * @return The SoundEvent
     */
    private static SoundEvent registerSound(String soundName) {
        final ResourceLocation soundID = new ResourceLocation(Main.MODID, soundName);
        return GameRegistry.register(new SoundEvent(soundID).setRegistryName(soundID));
    }
}