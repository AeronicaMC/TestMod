
package tld.testmod.client.gui;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.input.Keyboard;
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
    private GuiSlider sliderCommon;
    private double cachedSliderCommon = 33D;

    // Tab limits - allow limiting the viewable tabs
    private GuiSlider sliderViewableTabs;
    private static final int MIN_TABS = 1;
    private float cachedViewableTabCount = MAX_TABS;

    public GuiTest(GuiScreen guiScreenParent)
    {
        this.guiScreenParent = guiScreenParent;
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        for (int i = 0; i< MAX_TABS; i++)
        {
            childTabs[i] = new GuiChildTab(this);
        }
        Keyboard.enableRepeatEvents(true);
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

        sliderViewableTabs = new GuiSlider(1, padding, textCommon.y + textCommon.height + padding, width / 3, 20, "Num Tabs: ", "", (float) MIN_TABS, (float) MAX_TABS, cachedViewableTabCount, false, true);

        sliderCommon = new GuiSlider(0, sliderViewableTabs.width + 5, sliderViewableTabs.y, width / 3, 20, "Background: ", "%", 0D, 100D, cachedSliderCommon, false, true);

        for (int i = 0; i< MAX_TABS; i++)
        {
            buttonList.add(new GuiButtonExt(TAB_BTN_IDX + i, 5 + 20 * i, middle - 25, 20, 20, String.format("%d", i + 1)));
            childTabs[i].setLayout(middle, height - 5, height - 5 - middle, String.format("Child %d", i + 1));
            childTabs[i].initGui();
        }
        buttonList.add(sliderCommon);
        buttonList.add(sliderViewableTabs);
        reloadState();
    }

    private void reloadState()
    {
        if (!isStateCached) return;
        activeChildIndex = cachedActiveChildIndex;
        sliderViewableTabs.setValue(cachedViewableTabCount);
        sliderCommon.setValue(cachedSliderCommon);
        textCommon.setText(cachedTextCommon);
        updateButtons();
    }

    private void updateState()
    {
        cachedActiveChildIndex = activeChildIndex;
        cachedTextCommon = textCommon.getText();
        cachedViewableTabCount = (float) sliderViewableTabs.getValue();
        cachedSliderCommon = sliderCommon.getValue();

        updateButtons();
        isStateCached = true;
    }

    private void updateButtons()
    {
        for (GuiButton button : buttonList)
            if (button.id >= TAB_BTN_IDX && button.id < (MAX_TABS + TAB_BTN_IDX))
                button.enabled = (activeChildIndex + TAB_BTN_IDX) != button.id;

        for (GuiButton button : buttonList)
            if (button.id >= TAB_BTN_IDX && button.id < (MAX_TABS + TAB_BTN_IDX))
            {
                button.visible = (button.id) < (sliderViewableTabs.getValue() + TAB_BTN_IDX);
                if (activeChildIndex >= sliderViewableTabs.getValue())
                    activeChildIndex = (int) sliderViewableTabs.getValue() - 1;
            }
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
        if ((button.id >= TAB_BTN_IDX) && (button.id < (TAB_BTN_IDX + MAX_TABS)))
        {
            this.activeChildIndex = button.id - TAB_BTN_IDX;
            this.childTabs[activeChildIndex].onResize(mc, width, height);
            ModLogger.info("Tab: %d", (button.id - TAB_BTN_IDX + 1));
        }
        if (button.id == 0 || button.id == 1)
            updateState();
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
        // drawDefaultBackground();
        int color = (int) (sliderCommon.getValue() * 2.55D);
        this.drawGradientRect(0, 0, this.width, this.height, 0xCC000000 , 0xCC000000 + (color/2 << 16) + (color/2 << 8) + color);

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
        updateState();
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
