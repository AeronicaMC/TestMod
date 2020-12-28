package tld.testmod.common.storage.container;

import com.iciql.Db;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tld.testmod.ModLogger;
import tld.testmod.common.storage.dao.ModelDao;
import tld.testmod.common.storage.models.User;

import java.sql.SQLException;

import static tld.testmod.common.storage.ServerDataManager.getConnection;

public class ContainerDB extends Container
{
    private EntityPlayer player;
    private World world;
    private User[] users;

    public ContainerDB(EntityPlayer player, World world)
    {
        this.world = world;
        this.player = player;
        refresh();
    }


    private void refresh()
    {
        if (world.isRemote) return;
        try (Db db = Db.open(getConnection()))
        {
            ModelDao dao = db.open(ModelDao.class);
            users = dao.getAllUsers();
            for (User u : users)
            {
                ModLogger.info("***** H2: %s, %s", u.userName, u.uid.toString());
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }
}
