package tld.testmod.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

public abstract class GuiTabButton extends AbstractClickableElement
{
    protected String tabName;

    public GuiTabButton(Minecraft client, int width, int height, int top, int bottom, int left, int screenWidth, int screenHeight)
    {
        super(client, width, height, top, bottom, left, screenWidth, screenHeight);
    }

    public GuiTabButton(GuiScreen gui)
    {
        super(gui);
        this.mc = gui.mc;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        if (isEnableHighlightSelected() && index == selectedIndex && !doubleClick) return;
        setSelectedIndex(index);

        if (selectedIndex >= 0)
        {
            if (!doubleClick)
                selectedClickedCallback(selectedIndex);
            else
                selectedDoubleClickedCallback(selectedIndex);
        }
    }

    protected abstract void selectedClickedCallback(int selectedIndex);

    protected abstract void selectedDoubleClickedCallback(int selectedIndex);

    protected void setHighlightSelected(boolean state)
    {
        this.highlightSelected = state;
    }

    private boolean isEnableHighlightSelected()
    {
        return this.highlightSelected;
    }

    public int getSelectedIndex() { return selectedIndex; }

    public void setSelectedIndex(int index)
    {
        selectedIndex = index;
    }

    public String getTabName()
    {
        return tabName;
    }

    public void setTabName(String tabName)
    {
        this.tabName = tabName;
    }

    @Override
    protected void drawBackground()
    {
        Gui.drawRect(left - 1, top - 1, left + elementWidth + 1, top + elementHeight + 1, -6250336);
        Gui.drawRect(left, top, left + elementWidth, top + elementHeight, -16777216);
    }
}
