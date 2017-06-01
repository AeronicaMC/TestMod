package tld.testmod.common.world;

import java.util.Iterator;

import com.google.common.collect.EvictingQueue;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import tld.testmod.ModLogger;

public class StructureHelper
{

    public static final int MIN_DISTANCE = 100;
    public static EvictingQueue<BlockPos> stagePosQueue;
    
    private StructureHelper() {}
    
    static
    {
        stagePosQueue = EvictingQueue.create(100);
    }
    
    public static boolean canPlaceStage(BlockPos posIn)
    {
        boolean result = true;
        int comparisons = 0;
        if(stagePosQueue.isEmpty())
        {
            stagePosQueue.add(posIn);
            return true;
        }
        else 
        {
            Iterator<BlockPos> i = stagePosQueue.iterator();
            while(i.hasNext())
            {
                BlockPos pos = i.next();
                int distance = Math.abs((int) posIn.getDistance(pos.getX(), pos.getY(), pos.getZ()));
                comparisons++;
                if(distance < MIN_DISTANCE)
                {
                    ModLogger.info("canPlaceStage: NO! too close distance %d, ", distance);
                    result = false;
                }
            }
            if (result)
            {
                ModLogger.info("canPlaceStage: YES! Number of comparisons: %d, queue size: %d", comparisons, stagePosQueue.size());                
                stagePosQueue.add(posIn);
                return true;
            }
        }
        
        return false;
    }
    
    public static int getAverageGroundLevel(World worldIn, StructureBoundingBox boundingBox, StructureBoundingBox structurebb)
    {
        int i = 0;
        int j = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for (int k = boundingBox.minZ; k <= boundingBox.maxZ; ++k)
        {
            for (int l = boundingBox.minX; l <= boundingBox.maxX; ++l)
            {
                blockpos$mutableblockpos.setPos(l, 64, k);

                if (structurebb.isVecInside(blockpos$mutableblockpos))
                {
                    i += Math.max(worldIn.getTopSolidOrLiquidBlock(blockpos$mutableblockpos).getY(), worldIn.provider.getAverageGroundLevel() - 1);
                    ++j;
                }
            }
        }

        if (j == 0)
        {
            return -1;
        }
        else
        {
            return i / j;
        }
    }
}
