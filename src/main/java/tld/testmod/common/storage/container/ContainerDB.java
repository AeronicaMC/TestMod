package tld.testmod.common.storage.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class ContainerDB
{
    private EntityPlayer player;
    private World world;

    public ContainerDB(EntityPlayer player, World world)
    {
        this.world = world;
        this.player = player;
    }
}
