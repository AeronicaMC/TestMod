package tld.testmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tld.testmod.client.gui.util.GuiScrollingListOf;
import tld.testmod.common.storage.models.User;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.UUID;

public class GuiDB extends GuiContainer
{
    private final GuiScrollingListOf<User> userGuiScrollingList;

    public GuiDB(Container container)
    {
        super(container);
        this.mc = Minecraft.getMinecraft();
        this.fontRenderer = mc.fontRenderer;
        xSize = width;
        ySize = height;
        Keyboard.enableRepeatEvents(true);

        userGuiScrollingList = new GuiScrollingListOf<User>(this)
        {
            @Override
            protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
            {
                if (!isEmpty() && slotIdx >= 0 && slotIdx < size() && get(slotIdx) != null)
                {
                    User user = get(slotIdx);
                    String trimmedName = fontRenderer.trimStringToWidth(user.userName, listWidth - 10);
                    fontRenderer.drawStringWithShadow(trimmedName, (float) left + 3, slotTop, 0xADD8E6);
                }
            }

            @Override
            protected void selectedClickedCallback(int selectedIndex)
            {

            }

            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)
            {

            }
        };
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.clear();
        this.guiLeft = 0;
        this.guiTop = 0;
        int guiListWidth = (width - 15) * 3 / 4;
        // Library List
        int entryHeight = mc.fontRenderer.FONT_HEIGHT + 2;
        int left = 5;
        int titleTop = 20;
        int listTop = titleTop + 25;
        int listHeight = height - titleTop - entryHeight - 2 - 10 - 25 - 25;
        int listBottom = listTop + listHeight;
        int statusTop = listBottom + 4;
        int userListWidth = (width - 15) / 4;

        userGuiScrollingList.setLayout(entryHeight, userListWidth, listHeight, listTop, listBottom, left);
        User user01 = new User();
        user01.userName = "OneWill";
        user01.uid = UUID.randomUUID();

        User user02 = new User();
        user02.userName = "TwoCan";
        user02.uid = UUID.randomUUID();
        userGuiScrollingList.add(user01);
        userGuiScrollingList.add(user02);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 0:
                case 1:
                case 2:
                    break;
                default:
            }
        }
        // updateState();
        super.actionPerformed(button);
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
        userGuiScrollingList.drawScreen(mouseX, mouseY, partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1)
    {

    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        userGuiScrollingList.keyTyped(typedChar, keyCode);
        // updateState();
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void onResize(@Nonnull Minecraft mcIn, int w, int h)
    {
        //updateState();
        super.onResize(mcIn, w, h);
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int mouseX = Mouse.getEventX() * width / mc.displayWidth;
        int mouseY = height - Mouse.getEventY() * height / mc.displayHeight - 1;
        userGuiScrollingList.handleMouseInput(mouseX, mouseY);
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        //search.mouseClicked(mouseX, mouseY, mouseButton);
        //clearOnMouseLeftClicked(search, mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        //updateState();
    }
}
