package tld.testmod.common.utils;

import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

public class NBTHelper
{
    private static final String TAG_UUID_MSB = "uuid_msb";
    private static final String TAG_UUID_LSB = "uuid_lsb";

    private NBTHelper() { /* NOP */ }

    // UUID NBT Helpers

    public static UUID getUuidFromCompound(NBTTagCompound compound)
    {
        long msb = compound.getLong(TAG_UUID_MSB);
        long lsb = compound.getLong(TAG_UUID_LSB);
        return new UUID(msb, lsb);
    }

    public static void setUuidToCompound(NBTTagCompound compound, UUID uuid)
    {
        compound.setLong(TAG_UUID_MSB, uuid.getMostSignificantBits());
        compound.setLong(TAG_UUID_LSB, uuid.getLeastSignificantBits());
    }

    public static UUID getUuidFromTag(NBTTagCompound compound, String tagKey)
    {
        NBTTagCompound compoundTag = compound.getCompoundTag(tagKey);
        return getUuidFromCompound(compoundTag);
    }

    public static void setUuidToTag(UUID uuid, NBTTagCompound compound, String tagKey)
    {
        NBTTagCompound tagCompound = new NBTTagCompound();
        setUuidToCompound(tagCompound, uuid);
        compound.setTag(tagKey, tagCompound);
    }
}
