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

import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockLog;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.IWorldGenerator;
import tld.testmod.Main;
import tld.testmod.ModLogger;

public class WorldGenStageRegal implements IWorldGenerator
{

    public static final ResourceLocation STAGE_REGAL = new ResourceLocation(Main.prependModID("stage_regal"));
    
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if(!(world instanceof WorldServer))
            return;
        
        WorldServer sWorld = (WorldServer) world;
        
        int x = chunkX * 16 + random.nextInt(16);
        int z = chunkZ * 16 + random.nextInt(16);

        BlockPos xzPos = new BlockPos(x, 1, z);
        Biome biome = world.getBiomeForCoordsBody(xzPos);
        if(biome != Biomes.HELL && biome != Biomes.VOID && biome != Biomes.ROOFED_FOREST
                && biome != Biomes.MUSHROOM_ISLAND && biome != Biomes.MUSHROOM_ISLAND_SHORE
                && biome != Biomes.RIVER && biome != Biomes.BEACH)
        {
//            if(random.nextInt(2) == 0) {
                for (int rotation = 0; rotation < Rotation.values().length; rotation++)
                if (generateStageAt(sWorld, rotation, random, x, z))
                    break;
//            }
        }       
    }

    @SuppressWarnings("static-access")
    public static boolean generateStageAt(WorldServer world, int rotation, Random random, int xIn, int zIn)
    {
        final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.values()[rotation]);
        final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), STAGE_REGAL);      

        int i = xIn; // + random.nextInt(16);
        int k = zIn; // + random.nextInt(16);
        int j = world.getHeight(i, k);
        int airCount = 0;
        BlockPos pos = new BlockPos(i,j,k);
        BlockPos size = template.getSize();
        int horizontalArea = size.getX() * size.getZ();
        
        for(int y = 0; y < size.getY(); y++)
            for(int x = 0; x < size.getX(); x++)
                for(int z = 0; z < size.getZ(); z++)
                {
                    BlockPos checkPos = pos.add(template.transformedBlockPos(settings, new BlockPos(x, y, z)));
                    IBlockState checkState = world.getBlockState(checkPos);
                    IBlockState checkStateDown = world.getBlockState(checkPos.down());
                    if(!(checkState.getBlock() instanceof BlockAir))
                    {
                        // ModLogger.info("stage_regal OBSTRUCTED");
                        return false; // Obstructed, can't generate here
                    }
                    if(y == 0 && (checkStateDown.getBlock() instanceof BlockAir))
                    {
                        airCount++; // Air under structure
                    }
                    if(y == 0 && ( airCount * 100F / horizontalArea / 100F > 0.90F))
                    {
                        // ModLogger.info("stage_regal TOO MUCH AIR UNDERNEATH: airCount %d, area: %d, percent: %f", airCount, horizontalArea, ((airCount * 100F) / horizontalArea) / 100F); 
                        return false; // No spawning over mostly air
                    }
                    if(y == 0 && ((checkStateDown.getBlock() instanceof BlockLiquid) ||
                            (checkStateDown.getBlock() instanceof BlockLeaves) ||
                            (checkStateDown.getBlock() instanceof BlockLog)) )
                    {
                        // ModLogger.info("stage_regal NOT ON TREES OR WATER: block: %s", checkStateDown.getBlock().getRegistryName()); 
                        return false; // No spawning in trees, or on water!!
                    }
                }
        if(StructureHelper.canPlaceStage(pos))
        {
            ModLogger.info("*** Stage_Regal ***: position %s", pos.toString());
            template.addBlocksToWorld(world, pos, settings);

            // Fill in below the structure with stone
            for(int z = 0; z < size.getZ(); z++)
                for(int x = 0; x < size.getX(); x++)
                    for (int y = pos.getY()-1 ; y > 0 ; y--)
                    {
                        BlockPos checkPos = pos.add(template.transformedBlockPos(settings, new BlockPos(x, -y, z)));
                        IBlockState checkState = world.getBlockState(checkPos);
                        if(checkState.getBlock().canPlaceBlockAt(world, checkPos) || !checkState.getBlock().isCollidable()
                                || (checkState.getBlock() instanceof BlockAir) || checkState.getBlock().isPassable(world, checkPos)
                                || (checkState.getBlock() instanceof IGrowable) || (checkState.getBlock() instanceof IPlantable)) 
                            world.setBlockState(checkPos, Blocks.STONE.getDefaultState());  
                    }
            return true;
        }
        else
        {
            ModLogger.info("stage_regal TOO CLOSE TOGETHER");
            return false;
        }
    }
  
}
