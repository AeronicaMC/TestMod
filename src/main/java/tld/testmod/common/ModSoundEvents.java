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
    public static final SoundEvent ENTITY_TIMPANI_SQUISH_TINY;
    public static final SoundEvent ENTITY_TIMPANI_SQUISH_SMALL;
    public static final SoundEvent ENTITY_TIMPANI_SQUISH_MEDIUM;
    public static final SoundEvent ENTITY_TIMPANI_SQUISH_LARGE;
    //public static final SoundEvent ENTITY_TIMPANI_JUMP;
    /**
     * Register the {@link SoundEvent}s.
     */
    private ModSoundEvents() {}
    
    static {
        ENTITY_TIMPANI_SQUISH_TINY = registerSound("timpani_tiny");
        ENTITY_TIMPANI_SQUISH_SMALL = registerSound("timpani_small");
        ENTITY_TIMPANI_SQUISH_MEDIUM = registerSound("timpani_medium");
        ENTITY_TIMPANI_SQUISH_LARGE = registerSound("timpani_large");
        //ENTITY_TIMPANI_JUMP = registerSound("timpani_jump");
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