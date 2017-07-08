package tld.testmod.common.animation;

import net.minecraft.world.World;
import net.minecraftforge.client.model.animation.Animation;


public enum ModAnimation
{

    INSTANCE;
    
    /**
     * Get the global world time for the current tick, in seconds.
     */
    public static double getWorldTime(World world)
    {
        return getWorldTime(world, 0);
    }

    /**
     * Get the global world time for the current tick + partial tick progress, in seconds.
     */
    public static double getWorldTime(World world, float tickProgress)
    {
        long time = (long)world.getTotalWorldTime();
        return ((double)time + tickProgress) / 20;
    }

    /**
     * Get current partialTickTime.
     */
    public static float getPartialTickTime()
    {
        return Animation.getPartialTickTime();
    }
    
}