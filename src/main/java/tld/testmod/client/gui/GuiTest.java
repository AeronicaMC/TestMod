
package tld.testmod.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tld.testmod.ModLogger;
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

    private int maxTabs = 12;
    private GuiChildTab[] childTabs = new GuiChildTab[maxTabs];
    private int activeChildIndex;
    private int cachedActiveChildIndex;

    public GuiTest(GuiScreen guiScreenParent)
    {
        this.guiScreenParent = guiScreenParent;
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        for (int i=0; i<maxTabs; i++)
        {
            childTabs[i] = new GuiChildTab(this);
        }
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
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

        for (int i=0; i<maxTabs; i++)
        {
            buttonList.add(new GuiButton(200 + i, 5 + 20 * i, middle - 25, 20, 20, String.format("%d", i + 1)));
            childTabs[i].setLayout(middle, height - 5, height - 5 - middle, String.format("Child %d", i + 1));
            childTabs[i].initGui();
        }
        reloadState();
    }

    private void reloadState()
    {
        updateButtons();
        if (!isStateCached) return;
        activeChildIndex = cachedActiveChildIndex;
    }

    private void updateState()
    {
        cachedActiveChildIndex = activeChildIndex;
        updateButtons();
        isStateCached = true;
    }

    private void updateButtons()
    {
        for (int i=0; i<maxTabs; i++)
        {
            if (activeChildIndex == i)
                buttonList.get(i).enabled = false;
            else
                buttonList.get(i).enabled = true;
        }
    }

    @Override
    public void updateScreen()
    {
        childTabs[activeChildIndex].updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id > 199 && button.id < 200 + maxTabs)
        {
            this.activeChildIndex = button.id - 200;
            this.childTabs[activeChildIndex].onResize(mc, width, height);
            ModLogger.info("Tab: %d", button.id - 199);
        }
        updateState();
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
        drawRect(5,height/2, width-5, (height/2)+1, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        childTabs[activeChildIndex].drawScreen(mouseX, mouseY, partialTicks);
        ModGuiUtils.INSTANCE.drawHooveringHelp(this, hooverTexts, 0, 0, mouseX, mouseY);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
        childTabs[activeChildIndex].handleMouseInput();
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    /**
     * Called when a mouse button is released.
     */
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        super.onResize(mcIn, w, h);
        childTabs[activeChildIndex].onResize(mcIn, w, h);
    }
}
