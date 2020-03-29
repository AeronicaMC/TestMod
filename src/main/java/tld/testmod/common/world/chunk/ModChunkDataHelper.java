package tld.testmod.common.world.chunk;

import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import tld.testmod.library.Util;

import javax.annotation.Nullable;

public class ModChunkDataHelper
{
    @CapabilityInject(IModChunkData.class)
    private static final Capability<IModChunkData> MOD_CHUNK_DATA =  Util.nonNullInjected();

    private ModChunkDataHelper() { /* NOP */ }

    public static void setFunctional(Chunk chunk, boolean functional)
    {
        getImpl(chunk).setFunctional(functional);
        chunk.markDirty();
    }

    public static boolean isFunctional(Chunk chunk)
    {
        return getImpl(chunk).isFunctional();
    }

    public static void setString(Chunk chunk, String string)
    {
        getImpl(chunk).setString(string);
        chunk.markDirty();
    }

    public static String getString(Chunk chunk)
    {
        return getImpl(chunk).getString();
    }

    @Nullable
    private static IModChunkData getImpl(Chunk chunk)
    {
        IModChunkData chunkData;
        if (chunk.hasCapability(MOD_CHUNK_DATA, null))
            chunkData =  chunk.getCapability(MOD_CHUNK_DATA, null);
        else
            throw new RuntimeException("IModChunkData capability is null");
        return chunkData;
    }
}
