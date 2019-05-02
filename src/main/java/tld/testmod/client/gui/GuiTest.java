
package tld.testmod.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
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

    private static final int MAX_TABS = 12;
    private GuiChildTab[] childTabs = new GuiChildTab[MAX_TABS];
    private static final int TAB_BTN_IDX = 200;
    private int activeChildIndex;
    private int cachedActiveChildIndex;

    // Common Data
    GuiTextField textCommon;
    private String cachedTextCommon;

    public GuiTest(GuiScreen guiScreenParent)
    {
        this.guiScreenParent = guiScreenParent;
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        for (int i = 0; i< MAX_TABS; i++)
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

        textCommon = new GuiTextField(0, fontRenderer, padding, labelTitle.y + labelTitle.height + padding, width / 3, fontRenderer.FONT_HEIGHT +2);

        for (int i = 0; i< MAX_TABS; i++)
        {
            buttonList.add(new GuiButton(TAB_BTN_IDX + i, 5 + 20 * i, middle - 25, 20, 20, String.format("%d", i + 1)));
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
        textCommon.setText(cachedTextCommon);
    }

    private void updateState()
    {
        cachedActiveChildIndex = activeChildIndex;
        cachedTextCommon = textCommon.getText();
        updateButtons();
        isStateCached = true;
    }

    private void updateButtons()
    {
        for (GuiButton button : buttonList)
            if (button.id >= TAB_BTN_IDX && button.id < (MAX_TABS + TAB_BTN_IDX))
                button.enabled = (activeChildIndex + TAB_BTN_IDX) != button.id;
    }

    @Override
    public void updateScreen()
    {
        textCommon.updateCursorCounter();
        childTabs[activeChildIndex].updateScreen();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id >= TAB_BTN_IDX && button.id < TAB_BTN_IDX + MAX_TABS)
        {
            this.activeChildIndex = button.id - TAB_BTN_IDX;
            this.childTabs[activeChildIndex].onResize(mc, width, height);
            ModLogger.info("Tab: %d", button.id - TAB_BTN_IDX - 1);
        }
        updateState();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        childTabs[activeChildIndex].keyTyped(typedChar, keyCode);
        textCommon.textboxKeyTyped(typedChar, keyCode);
        updateState();
        super.keyTyped(typedChar, keyCode);
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
        textCommon.drawTextBox();
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
        textCommon.mouseClicked(mouseX, mouseY, mouseButton);
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
