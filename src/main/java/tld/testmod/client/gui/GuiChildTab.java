package tld.testmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import tld.testmod.ModLogger;
import tld.testmod.client.gui.util.GuiLabelMX;

import java.io.IOException;

public class GuiChildTab extends GuiScreen
{
    private final GuiTest guiTest;
    private int top;
    private int bottom;
    private int childHeight;
    private boolean isStateCached;

    // Tab content
    private String childName;
    private GuiTextField textTest;
    private String cachedTextTest;
    private GuiLabelMX labelCommon;

    public GuiChildTab(GuiTest guiTest)
    {
        this.guiTest = guiTest;
        this.mc = guiTest.mc;
        this.fontRenderer = guiTest.mc.fontRenderer;

    }

    public void setLayout(int top, int bottom, int childHeight, String childName)
    {
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
        textTest = new GuiTextField(0, fontRenderer, 5, bottom - 20, width /3 , fontRenderer.FONT_HEIGHT + 2);
        textTest.setMaxStringLength(80);
        textTest.setEnabled(true);
        textTest.setFocused(true);
        labelCommon = new GuiLabelMX(fontRenderer, 1, 5, bottom -40, width /3, fontRenderer.FONT_HEIGHT + 2, 0xAAAAFF);
        reloadState();
    }

    private void reloadState()
    {
        updateButtons();
        if (!isStateCached) return;
        textTest.setText(cachedTextTest);
    }

    private void updateState()
    {
        cachedTextTest = textTest.getText();
        updateButtons();
        isStateCached = true;
    }

    private void updateButtons()
    {
        // NOP
    }

    @Override
    public void updateScreen()
    {
        textTest.updateCursorCounter();
        this.labelCommon.setLabel(guiTest.textCommon.getText());
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        drawRect(5,bottom-1, width-5, bottom, -1);
        textTest.drawTextBox();
        labelCommon.drawLabel(mc, mouseX, mouseY);
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
        updateState();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        textTest.textboxKeyTyped(typedChar, keyCode);

        updateState();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        textTest.mouseClicked(mouseX, mouseY, mouseButton);
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
