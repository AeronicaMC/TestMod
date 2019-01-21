package tld.testmod.common.world;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import tld.testmod.Main;

public class ModGlobalWorldSaveData extends WorldSavedData
{
    private static final String MOD_WORLD_GLOBAL_DATA = Main.MOD_ID + "_mod_world_global_data";
    private static final String KEY_NAME = "name";
    private static final String KEY_NUMBER = "number";
    private static final String KEY_IS_SOMETHING = "isSomething";

    private String name;
    private int number;
    private boolean isSomething;

    private ModGlobalWorldSaveData()
    {
        super(MOD_WORLD_GLOBAL_DATA);
        name = "";
        number = 0;
        isSomething = false;
    }

    public ModGlobalWorldSaveData(String s)
    {
        super(s);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        markDirty();
    }

    public int getNumber()
    {
        return number;
    }

    public void setNumber(int number)
    {
        this.number = number;
        markDirty();
    }

    public boolean isSomething()
    {
        return isSomething;
    }

    public void setSomething(boolean something)
    {
        isSomething = something;
       markDirty();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
            name = nbt.getString(KEY_NAME);
            number = nbt.getInteger(KEY_NUMBER);
            isSomething = nbt.getBoolean(KEY_IS_SOMETHING);
    }

    // Remember to use markDirty()
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        compound.setString(KEY_NAME, name);
        compound.setInteger(KEY_NUMBER, number);
        compound.setBoolean(KEY_IS_SOMETHING, isSomething);
        return compound;
    }

    public static ModGlobalWorldSaveData get(World world)
    {
        MapStorage storage = world.getMapStorage();
        if (storage == null) return null;

        ModGlobalWorldSaveData instance = (ModGlobalWorldSaveData) storage.getOrLoadData(ModGlobalWorldSaveData.class, MOD_WORLD_GLOBAL_DATA);

        if (instance == null)
        {
            instance = new ModGlobalWorldSaveData();
            storage.setData(MOD_WORLD_GLOBAL_DATA, instance);
        }
        return instance;
    }
}
