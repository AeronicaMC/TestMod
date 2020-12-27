
package tld.testmod.common.handlers;


import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import tld.testmod.client.gui.GuiDB;
import tld.testmod.client.gui.GuiGuid;
import tld.testmod.client.gui.GuiTest;
import tld.testmod.common.storage.container.ContainerDB;

public class GUIHandler implements IGuiHandler
{

    private GUIHandler() {}
    private static class GUIHandlerHolder {private static final GUIHandler INSTANCE = new GUIHandler();}
    public static GUIHandler getInstance() {return GUIHandlerHolder.INSTANCE;}

    @Override
    public Object getServerGuiElement(int guiID, EntityPlayer playerIn, World worldIn, int x, int y, int z)
    {
        switch (guiID)
        {
            case GuiGuid.GUI_DB:
                return new ContainerDB(playerIn, worldIn);

            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int guiID, EntityPlayer playerIn, World worldIn, int x, int y, int z)
    {
        switch (guiID)
        {
            case GuiGuid.GUI_DB:
                return new GuiDB(null);

            case GuiGuid.GUI_TEST:
                return new GuiTest(null);

        default:
            return null;
        }
    }
}
