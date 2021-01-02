package tld.testmod.common.storage.capability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tld.testmod.Main;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class MusicDBCapability
{
    @CapabilityInject(IMusicDB.class)
    private static final Capability<IMusicDB> MUSIC_DB_CAP = nonNullInjected();

    @SuppressWarnings("ConstantConditions")
    public static <T> T nonNullInjected()
    {
        return null;
    }

    public static void register()
    {
        CapabilityManager.INSTANCE.register(IMusicDB.class, new Storage(), new Factory());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public static class EventHandler
    {
        @SubscribeEvent
        public void onEntityConstruct(final AttachCapabilitiesEvent<Entity> event)
        {
            if (event.getObject() instanceof EntityPlayer)
            {
                event.addCapability(new ResourceLocation(Main.MOD_ID, "music_db"), new ICapabilitySerializable<NBTTagCompound>()
                {
                    final IMusicDB musicDBInst = MUSIC_DB_CAP.getDefaultInstance();
                    @Override
                    public NBTTagCompound serializeNBT()
                    {
                        return (NBTTagCompound) MUSIC_DB_CAP.getStorage().writeNBT(MUSIC_DB_CAP, musicDBInst, null);
                    }

                    @Override
                    public void deserializeNBT(NBTTagCompound nbt)
                    {
                        MUSIC_DB_CAP.getStorage().readNBT(MUSIC_DB_CAP, musicDBInst, null, nbt);
                    }

                    @Override
                    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
                    {
                        return capability == MUSIC_DB_CAP;
                    }

                    @SuppressWarnings("unchecked")
                    @Nullable
                    @Override
                    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
                    {
                        return (capability == MUSIC_DB_CAP) ? (T) musicDBInst : null;
                    }
                });
            }
        }

        @SubscribeEvent
        public void onPlayerClone(PlayerEvent.Clone event)
        {
            IMusicDB dead = event.getOriginal().getCapability(MUSIC_DB_CAP, null);
            IMusicDB live = event.getEntityPlayer().getCapability(MUSIC_DB_CAP, null);
            if (live != null && dead != null)
            {
                if(dead.isSessionOpen())
                    live.closeSession();
            }
        }

        @SubscribeEvent
        public void onEntityJoinWorld(EntityJoinWorldEvent event)
        {
//            if (event.getEntity() instanceof EntityPlayerMP)
//            {
//                IMusicDB inst = event.getEntity().getCapability(MUSIC_DB_CAP, null);
//                if (inst != null)
//                    MusicDBHelper.syncAll((EntityPlayer) event.getEntity());
//            }
        }

        @SubscribeEvent
        public void onPlayerLoggedInEvent(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event)
        {
            IMusicDB inst = event.player.getCapability(MUSIC_DB_CAP, null);
            if (inst != null)
                MusicDBHelper.syncAll(event.player);
        }
    }

    public static class Factory implements Callable<IMusicDB>
    {
        @Override
        public IMusicDB call()
        {
            return new MusicDBImpl();
        }
    }

    public static class Storage implements Capability.IStorage<IMusicDB>
    {
        static final String KEY_SESSION = "session";

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<IMusicDB> capability, IMusicDB instance, EnumFacing side)
        {
            NBTTagCompound properties = new NBTTagCompound();
            properties.setBoolean(KEY_SESSION, instance.isSessionOpen());
            return properties;
        }

        @Override
        public void readNBT(Capability<IMusicDB> capability, IMusicDB instance, EnumFacing side, NBTBase nbt)
        {
            NBTTagCompound properties = (NBTTagCompound) nbt;
            if (properties.getBoolean(KEY_SESSION))
                instance.openSession();
            else
                instance.closeSession();
        }
    }

}
