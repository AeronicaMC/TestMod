package tld.testmod.common.world;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockCarpet;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.template.PlacementSettings;
import net.minecraft.world.gen.structure.template.Template;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.IWorldGenerator;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.common.entity.living.EntityTestSkeleton;

/**
 * {@link IWorldGenerator}
 * @see <a ref="https://www.reddit.com/r/feedthebeast/comments/5x0twz/investigating_extreme_worldgen_lag/">Reddit - investigating_extreme_worldgen_lag</a>
 * 
 */
public class WorldGenStageRegal implements IWorldGenerator
{

    public static final ResourceLocation STAGE_REGAL = new ResourceLocation(Main.prependModID("stage_regal"));
    
    /**
     * {@link #generate(Random, int, int, World, IChunkGenerator, IChunkProvider)}
     */
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        if(!(world instanceof WorldServer))
            return;
        
        WorldServer sWorld = (WorldServer) world;
        // https://www.reddit.com/r/feedthebeast/comments/5x0twz/investigating_extreme_worldgen_lag/
        int x = chunkX * 16 + 8; // The all important offset of +8
        int z = chunkZ * 16 + 8; // The all important offset of +8

        BlockPos xzPos = new BlockPos(x, 1, z);
        Biome biome = world.getBiomeForCoordsBody(xzPos);
        if(biome != Biomes.HELL && biome != Biomes.VOID && biome != Biomes.ROOFED_FOREST
                && biome != Biomes.MUSHROOM_ISLAND && biome != Biomes.MUSHROOM_ISLAND_SHORE
                && biome != Biomes.RIVER && biome != Biomes.BEACH)
        {
            if(random.nextInt(5) == 0) {
                for (int rotation = 0; rotation < Rotation.values().length; rotation++)
                if (generateStageAt(sWorld, rotation, random, x, z))
                {
                    Chunk chunk = chunkProvider.getLoadedChunk(chunkX, chunkZ);
                    chunk.resetRelightChecks();
                    break;
                }
            }
        }       
    }

    @SuppressWarnings("static-access")
    public static boolean generateStageAt(WorldServer world, int rotation, Random random, int xIn, int zIn)
    {
        final PlacementSettings settings = new PlacementSettings().setRotation(Rotation.values()[rotation]);
        final Template template = world.getSaveHandler().getStructureTemplateManager().getTemplate(world.getMinecraftServer(), STAGE_REGAL);      
                    
        int i = xIn; // - template.getSize().getX()/2; // + random.nextInt(16);
        int k = zIn; // - template.getSize().getZ()/2; // + random.nextInt(16); 
        
        int j = world.getHeight(i, k);
        int airCount = 0;
        

        BlockPos zeroPos = template.getZeroPositionWithTransform(new BlockPos(i,j,k), Mirror.NONE, settings.getRotation());
        BlockPos size = template.getSize();
        
        int agl = StructureHelper.getAverageGroundLevel(world, new StructureBoundingBox(new Vec3i(i-8, 64, k-8), new Vec3i(i+8, 128, k+8)),
                new StructureBoundingBox(new Vec3i(i-size.getX()/2, 64, k-size.getZ()/2), new Vec3i(i+size.getX()/2, 64, k+size.getZ()/2)));
        zeroPos = new BlockPos(i-size.getX()/2,agl,k-size.getZ()/2);
        
        int horizontalArea = size.getX() * size.getZ();
        
        for(int y = 0; y < size.getY(); y++)
            for(int x = 0; x < size.getX(); x++)
                for(int z = 0; z < size.getZ(); z++)
                {
                    BlockPos checkPos = zeroPos.add(template.transformedBlockPos(settings, new BlockPos(x, y, z)));
                    IBlockState checkState = world.getBlockState(checkPos);
                    IBlockState checkStateDown = world.getBlockState(checkPos.down());
                    if(!(checkState.getBlock() instanceof BlockAir))
                    {
                        // ModLogger.info("stage_regal OBSTRUCTED");
                        return false; // Obstructed, can't generate here
                    }
                    if(y == 0 && (checkStateDown.getMaterial().isLiquid() ||
                            (checkStateDown.getBlock() instanceof BlockLeaves) ||
                            (checkStateDown.getBlock() instanceof BlockLog) ||
                            (checkStateDown.getBlock() instanceof BlockPlanks) ||
                            (checkStateDown.getBlock() instanceof BlockSlab)) )
                    {
                        // ModLogger.info("stage_regal NOT ON TREES OR WATER: block: %s", checkStateDown.getBlock().getRegistryName()); 
                        return false; // No spawning in trees, or on water!!
                    }
                }
        if(StructureHelper.canPlaceStage(zeroPos))
        {
            ModLogger.info("*** Stage_Regal ***: position %s", zeroPos.toString());
            template.addBlocksToWorld(world, zeroPos, settings);

            // Fill in below the structure with stone
            for(int z = 0; z < size.getZ(); z++)
                for(int x = 0; x < size.getX(); x++)
                    for (int y = zeroPos.getY()-1 ; y > 0 ; y--)
                    {
                        BlockPos checkPos = zeroPos.add(template.transformedBlockPos(settings, new BlockPos(x, -y, z)));
                        IBlockState checkState = world.getBlockState(checkPos);
                        if(checkState.getBlock().canPlaceBlockAt(world, checkPos) || !checkState.getBlock().isCollidable()
                                || (checkState.getBlock() instanceof BlockAir) || checkState.getBlock().isPassable(world, checkPos)
                                || (checkState.getBlock() instanceof IGrowable) || (checkState.getBlock() instanceof IPlantable)) 
                            world.setBlockState(checkPos, Blocks.STONE.getDefaultState());  
                    }
            replaceDataBlocks(world, zeroPos, random, template, settings);
            return true;
        }
        else
        {
            ModLogger.info("stage_regal TOO CLOSE TOGETHER");
            return false;
        }
    }
  
    private static void replaceDataBlocks(World worldIn, BlockPos posIn, Random randomIn, Template templateIn, PlacementSettings settingsIn)
    {
        Map<BlockPos, String> dataBlocks = templateIn.getDataBlocks(posIn, settingsIn);
        for(Entry<BlockPos, String> entry : dataBlocks.entrySet()) {
            String[] tokens = entry.getValue().split(" ");
            if(tokens.length == 0)
                return;

            BlockPos dataPos = entry.getKey();
            EntityTestSkeleton skeleton;

            ModLogger.info("stage_regal dataEntry: %s", entry);
            switch(tokens[0])
            {
            case "skeleton":
                skeleton = new EntityTestSkeleton(worldIn);
                skeleton.setPosition(dataPos.getX() + 0.5, dataPos.getY() + 0.1, dataPos.getZ() + 0.5);
                skeleton.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.GOLDEN_CHESTPLATE));
                skeleton.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_HELMET));
                skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
                skeleton.setArrowCountInEntity(64);
                skeleton.setHomePosAndDistance(dataPos.add(0.5,0,0.5), 25);
                worldIn.spawnEntity(skeleton);
                ModLogger.info("stage_regal Skeleton: %s", skeleton);
                break;
            case "loot":
                float chance = tokens.length == 3 ? 1F : 0.75F;

                if(randomIn.nextFloat() <= chance)
                {
                    String chestOrientation = tokens[1];
                    EnumFacing chestFacing = settingsIn.getRotation().rotate(EnumFacing.byName(chestOrientation));
                    IBlockState chestState = Blocks.CHEST.getDefaultState().withProperty(BlockChest.FACING, chestFacing);
                    worldIn.setBlockState(dataPos, chestState);

                    TileEntity tile = worldIn.getTileEntity(dataPos);
                    if(tile != null && tile instanceof TileEntityLockableLoot)
                        ((TileEntityLockableLoot) tile).setLootTable(LootTableList.CHESTS_SIMPLE_DUNGEON, randomIn.nextLong());
                }
                else
                {
                    worldIn.setBlockState(dataPos, Blocks.CARPET.getDefaultState().withProperty(BlockCarpet.COLOR, EnumDyeColor.RED));
                }
                break;
            }
        }   
    }
}
