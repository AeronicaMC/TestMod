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
package tld.testmod.common.world;

import java.util.Iterator;

import com.google.common.collect.EvictingQueue;

import net.minecraft.util.math.BlockPos;
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
}
