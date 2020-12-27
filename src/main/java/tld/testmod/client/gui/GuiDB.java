package tld.testmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class GuiDB extends GuiScreen
{
    private GuiScreen guiScreenParent;

    public GuiDB(GuiScreen guiScreenParent)
    {
        this.guiScreenParent = guiScreenParent;
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        Keyboard.enableRepeatEvents(true);
    }

    @Override
    public void initGui()
    {

    }

}
