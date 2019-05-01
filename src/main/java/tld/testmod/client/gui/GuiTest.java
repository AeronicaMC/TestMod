
package tld.testmod.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import tld.testmod.client.gui.util.GuiLabelMX;
import tld.testmod.client.gui.util.IHooverText;
import tld.testmod.client.gui.util.ModGuiUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiTest extends GuiScreen
{
    private List<IHooverText> hooverTexts = new ArrayList<>();
    private GuiLabelMX labelTitle;
    private GuiScreen guiScreenParent;
    private boolean isStateCached;


    private int activePartIndex;


    public GuiTest(GuiScreen guiScreenParent)
    {
        this.guiScreenParent = guiScreenParent;
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
    }

    @Override
    public void initGui()
    {
        hooverTexts.clear();
        int singleLineHeight = mc.fontRenderer.FONT_HEIGHT + 2;
        int padding = 4;
        int titleTop = padding;
        int left = padding;
        int titleWidth = fontRenderer.getStringWidth("Gui Test");
        int titleX = (width / 2) - (titleWidth / 2);
        int middle = height / 2;

        labelTitle = new GuiLabelMX(fontRenderer, 1, titleX, titleTop, titleWidth, singleLineHeight, -1);
        labelTitle.setLabel("Gui Test");

        reloadState();
    }

    private void reloadState()
    {
        if (!isStateCached) return;

    }

    private void updateState()
    {
        isStateCached = true;
    }

    @Override
    public void updateScreen()
    {

        super.updateScreen();
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawDefaultBackground();

        labelTitle.drawLabel(mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
        ModGuiUtils.INSTANCE.drawHooveringHelp(this, hooverTexts, 0, 0, mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();

    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {

    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {

    }
}
