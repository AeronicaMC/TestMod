package tld.testmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import tld.testmod.ModLogger;

import java.io.IOException;

public class GuiChildTab extends GuiScreen
{
    private final GuiTest guiTest;
    private int top;
    private int bottom;
    private int childHeight;

    // Tab content
    private final String childName;

    public GuiChildTab(GuiTest guiTest, int top, int bottom, int childHeight, String childName)
    {
        this.guiTest = guiTest;
        this.mc = guiTest.mc;
        this.fontRenderer = guiTest.mc.fontRenderer;
        this.width = guiTest.width;
        this.height = guiTest.height;
        this.top = top;
        this.bottom = bottom;
        this.childHeight = childHeight;
        this.childName = childName;
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        buttonList.add(new GuiButton(1,(width / 2) - 75, top + 5,150, 20, childName));
    }
    @Override
    public void updateScreen()
    {
        // NOP
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawRect(5,bottom-1, width-5, bottom, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
            case 1:
                ModLogger.info("Button Clicked: %s", childName);
                break;
            default:
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        super.handleMouseInput();
    }

    @Override
    public void handleKeyboardInput() throws IOException
    {
        super.handleKeyboardInput();
    }

    @Override
    public void onResize(Minecraft mcIn, int w, int h)
    {
        super.onResize(mcIn, w, h);
    }
}
