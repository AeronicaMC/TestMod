package tld.testmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraftforge.server.command.TextComponentHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tld.testmod.client.gui.util.GuiButtonMX;
import tld.testmod.client.gui.util.GuiLabelMX;
import tld.testmod.client.gui.util.GuiScrollingListOf;
import tld.testmod.client.gui.util.GuiTabButton;
import tld.testmod.common.storage.capability.IMusicDB;
import tld.testmod.common.storage.capability.MusicDBHelper;
import tld.testmod.common.storage.capability.RequestType;
import tld.testmod.common.storage.models.User;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.server.MusicDBServerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class GuiDB extends GuiScreen
{
    private final GuiScrollingListOf<User> userGuiScrollingList;
    private GuiButtonMX buttonToggle;
    private GuiLabelMX labelStatus;
    private GuiTabButton tabButton;
    private final IMusicDB musicDB;

    public GuiDB()
    {
        this.mc = Minecraft.getMinecraft();
        musicDB = MusicDBHelper.getImpl(mc.player);
        this.fontRenderer = mc.fontRenderer;
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
                toggleSession();
            }
        };

        tabButton = new GuiTabButton(this) {
            @Override
            protected void drawSlot(int entryRight, int slotTop, int slotBuffer, Tessellator tess)
            {
                String trimmedName = fontRenderer.trimStringToWidth(tabName, elementWidth - 10);
                fontRenderer.drawStringWithShadow(trimmedName, (float) left + 3, slotTop, highlightSelected ? 0xFFFF00 : 0xADD8E6);
            }

            @Override
            protected void selectedClickedCallback(int selectedIndex)
            {
                toggleSession();
            }

            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)
            {
                toggleSession();
            }
        };
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.clear();
        int guiListWidth = (width - 15) * 3 / 4;
        // Library List
        int entryHeight = mc.fontRenderer.FONT_HEIGHT + 2;
        int left = 5;
        int titleTop = 20;
        int listTop = titleTop + 25;
        int listHeight = height - titleTop - entryHeight - 2 - 10 - 25 - 25;
        int listBottom = listTop + listHeight;
        int tabHeight = listTop - titleTop - 4;
        int statusTop = listBottom + 4;
        int userListWidth = (width - 15) / 4;

        userGuiScrollingList.setLayout(entryHeight, userListWidth, listHeight, listTop, listBottom, left);
        userGuiScrollingList.clear();

        tabButton.setLayout(entryHeight, userListWidth, tabHeight, titleTop, userGuiScrollingList.getRight()+5);
        tabButton.setTabName("Some Tab");

        if (musicDB != null)
        {
            User[] users = musicDB.getUsers();
            if (users != null)
                userGuiScrollingList.addAll(Arrays.asList(users));
        }

        labelStatus = new GuiLabelMX(fontRenderer, 0, userGuiScrollingList.getRight()+5, userGuiScrollingList.getTop(), width - 5 - userGuiScrollingList.getRight(), 10,0xFFFFFF);
        labelStatus.setLabel(String.format("Session open: %s", MusicDBHelper.isSessionOpen(mc.player)));
        buttonToggle = new GuiButtonMX(0, userGuiScrollingList.getRight() + 5, labelStatus.y + labelStatus.height + 5, "Toggle");
        addButton(buttonToggle);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            switch (button.id)
            {
                case 0:
                    toggleSession();
                    break;
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
        labelStatus.setLabel(String.format("Session open: %s", MusicDBHelper.isSessionOpen(mc.player)));
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
        tabButton.drawScreen(mouseX, mouseY, partialTicks);
        labelStatus.drawLabel(mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
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
        tabButton.handleMouseInput(mouseX, mouseY);
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

    private void toggleSession()
    {
        PacketDispatcher.sendToServer(new MusicDBServerRequest(RequestType.TOGGLE));
    }
}
