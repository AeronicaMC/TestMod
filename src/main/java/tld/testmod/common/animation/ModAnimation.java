package tld.testmod.common.animation;

import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;

public enum ModAnimation
{

    INSTANCE;
    static long timeOffset;
    static int lastDimension = Integer.MAX_VALUE;
    
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
        int dimension = world.provider.getDimension();
        if (dimension != lastDimension)
        {
            timeOffset = world.getTotalWorldTime();
            lastDimension = dimension;
        }
        //long diff = world.getTotalWorldTime() - timeOffset;
        //ModLogger.info("Animation#getWorldTime: Dimension: %d, time: %d, offset %d, diff: %d", lastDimension, world.getTotalWorldTime(), timeOffset, diff);
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
