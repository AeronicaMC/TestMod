package tld.testmod.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tld.testmod.Main;

public class ModSoundEvents {
    
    public static final SoundEvent ENTITY_TINY_TIMPANI_SQUISH = registerSound("entity.tiny.timpani.squish");
    public static final SoundEvent ENTITY_MEDIUM_TIMPANI_SQUISH = registerSound("entity.medium.timpani.squish");
    public static final SoundEvent ENTITY_LARGE_TIMPANI_SQUISH = registerSound("entity.large.timpani.squish");
    public static final SoundEvent ENTITY_TIMPANI_JUMP = registerSound("entity.timpani.jump");
    public static final SoundEvent ENTITY_TINY_TIMPANI_HURT = registerSound("entity.small.timpani.hurt");
    public static final SoundEvent ENTITY_TINY_TIMPANI_DEATH = registerSound("entity.small.timpani.death");
    public static final SoundEvent ENTITY_TIMPANI_HURT = registerSound("entity.timpani.hurt");
    public static final SoundEvent ENTITY_TIMPANI_DEATH = registerSound("entity.timpani.death");
    public static final SoundEvent BELL = registerSound("block.bell");
    

    private ModSoundEvents() {}
    private static class InstanceHolder
    {
        public static final ModSoundEvents INSTANCE = new ModSoundEvents();
    }
    public static ModSoundEvents getInstance()
    {
        return InstanceHolder.INSTANCE;
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