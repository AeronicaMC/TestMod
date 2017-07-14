package tld.testmod.common.animation;

import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;
import tld.testmod.ModLogger;

public enum ModAnimation
{

    INSTANCE;
    static long timeOffset;
    static Object lastWorld;
    
    /**
     * Get the global world time for the current tick, in seconds.
     */
    public static float getWorldTime(World world)
    {
        return getWorldTime(world, 0);
    }

    /**
     * Get the global world time for the current tick + partial tick progress, in seconds.
     */
    public static float getWorldTime(World world, float tickProgress)
    {
        if (!world.equals(lastWorld))
        {
            timeOffset = world.getTotalWorldTime();
            lastWorld = world;
        }
        long diff = world.getTotalWorldTime() - timeOffset;
        ModLogger.info("Animation#getWorldTime: World: %s, time: %d, offset %d, diff: %d", lastWorld, world.getTotalWorldTime(), timeOffset, diff);
        return (world.getTotalWorldTime() - timeOffset  + tickProgress) / 20;
    }

    /**
     * Get current partialTickTime.
     */
    public static float getPartialTickTime()
    {
        return Animation.getPartialTickTime();
    }
    
}
