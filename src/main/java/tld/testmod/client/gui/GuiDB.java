package tld.testmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tld.testmod.client.gui.util.GuiButtonMX;
import tld.testmod.client.gui.util.GuiLabelMX;
import tld.testmod.client.gui.util.GuiScrollingListOf;
import tld.testmod.common.storage.capability.IMusicDB;
import tld.testmod.common.storage.capability.MusicDBHelper;
import tld.testmod.common.storage.capability.RequestType;
import tld.testmod.common.storage.models.*;
import tld.testmod.network.PacketDispatcher;
import tld.testmod.network.server.MusicDBServerRequest;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Arrays;

public class GuiDB extends GuiScreen
{
    private final GuiScrollingListOf<User> userGSL;
    private final GuiScrollingListOf<PlayList> playListGSL;
    private final GuiScrollingListOf<PlayListEntry> playListEntryGSL;
    private final GuiScrollingListOf<Song> songGSL;
    private final GuiScrollingListOf<Tag> tagGSL;
    private GuiButtonMX buttonToggle;
    private GuiLabelMX labelStatus;
    private final IMusicDB musicDB;

    public GuiDB()
    {
        this.mc = Minecraft.getMinecraft();
        musicDB = MusicDBHelper.getImpl(mc.player);
        this.fontRenderer = mc.fontRenderer;
        Keyboard.enableRepeatEvents(true);

        userGSL = new GuiScrollingListOf<User>(this)
        {
            @Override
            protected void selectedClickedCallback(int selectedIndex) { /* NOP */ }
            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)
            {
                toggleSession();
            }
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
        };

        playListGSL = new GuiScrollingListOf<PlayList>(this) {
            @Override
            protected void selectedClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
            {
                if (!isEmpty() && slotIdx >= 0 && slotIdx < size() && get(slotIdx) != null)
                {
                    PlayList playList = get(slotIdx);
                    String trimmedName = fontRenderer.trimStringToWidth(playList.playListName, listWidth - 10);
                    fontRenderer.drawStringWithShadow(trimmedName, (float) left + 3, slotTop, 0xADD8E6);
                }
            }
        };

        playListEntryGSL = new GuiScrollingListOf<PlayListEntry>(this) {
            @Override
            protected void selectedClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
            {
                if (!isEmpty() && slotIdx >= 0 && slotIdx < size() && get(slotIdx) != null)
                {
                    PlayListEntry entry = get(slotIdx);
                    String allIds = String.format("%02d, %02d, %02d" ,entry.eid, entry.pid, entry.sid);
                    String trimmedName = fontRenderer.trimStringToWidth(allIds, listWidth - 10);
                    fontRenderer.drawStringWithShadow(trimmedName, (float) left + 3, slotTop, 0xADD8E6);
                }
            }
        };

        songGSL = new GuiScrollingListOf<Song>(this) {
            @Override
            protected void selectedClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
            {
                if (!isEmpty() && slotIdx >= 0 && slotIdx < size() && get(slotIdx) != null)
                {
                    Song song = get(slotIdx);
                    String trimmedName = fontRenderer.trimStringToWidth(song.songTitle, listWidth - 10);
                    fontRenderer.drawStringWithShadow(trimmedName, (float) left + 3, slotTop, 0xADD8E6);
                }
            }
        };

        tagGSL = new GuiScrollingListOf<Tag>(this) {
            @Override
            protected void selectedClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void selectedDoubleClickedCallback(int selectedIndex)  { /* NOP */ }

            @Override
            protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
            {
                if (!isEmpty() && slotIdx >= 0 && slotIdx < size() && get(slotIdx) != null)
                {
                    Tag tag = get(slotIdx);
                    String trimmedName = fontRenderer.trimStringToWidth(tag.tagName, listWidth - 10);
                    fontRenderer.drawStringWithShadow(trimmedName, (float) left + 3, slotTop, 0xADD8E6);
                }
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
        int userListWidth = (width - 25) / 4;

        userGSL.setLayout(entryHeight, userListWidth, listHeight, listTop, listBottom, left);
        userGSL.clear();
        playListGSL.setLayout(entryHeight, userListWidth, listHeight/2 , listTop, listTop + (listHeight/2), userGSL.getRight() + 5);
        playListGSL.clear();
        playListEntryGSL.setLayout(entryHeight, userListWidth, listBottom - playListGSL.getBottom() - 5, playListGSL.getBottom() + 5, listBottom, userGSL.getRight() + 5);
        playListEntryGSL.clear();
        songGSL.setLayout(entryHeight, userListWidth, listHeight, listTop, listBottom, playListGSL.getRight() + 5);
        songGSL.clear();
        tagGSL.setLayout(entryHeight, userListWidth, listHeight, listTop, listBottom, songGSL.getRight() + 5);
        tagGSL.clear();

        if (musicDB != null)
        {
            User[] users = musicDB.getUsers();
            userGSL.addAll(Arrays.asList(users));
            PlayList[] playLists = musicDB.getPlaylists();
            playListGSL.addAll(Arrays.asList(playLists));
            PlayListEntry[] playListEntries = musicDB.getPlayListEntries();
            playListEntryGSL.addAll(Arrays.asList(playListEntries));
            Song[] songs = musicDB.getSongs();
            songGSL.addAll(Arrays.asList(songs));
            Tag[] tags = musicDB.getTags();
            tagGSL.addAll(Arrays.asList(tags));
        }

        labelStatus = new GuiLabelMX(fontRenderer, 0, left, titleTop, userListWidth, 10, 0xFFFFFF);
        labelStatus.setLabel(String.format("Session open: %s", MusicDBHelper.isSessionOpen(mc.player)));
        buttonToggle = new GuiButtonMX(0, tagGSL.getLeft(), titleTop, "Toggle");
        buttonToggle.setWidth(userListWidth);
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
        userGSL.drawScreen(mouseX, mouseY, partialTicks);
        playListGSL.drawScreen(mouseX, mouseY, partialTicks);
        playListEntryGSL.drawScreen(mouseX, mouseY, partialTicks);
        songGSL.drawScreen(mouseX, mouseY, partialTicks);
        tagGSL.drawScreen(mouseX, mouseY, partialTicks);
        labelStatus.drawLabel(mc, mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        userGSL.keyTyped(typedChar, keyCode);
        playListGSL.keyTyped(typedChar, keyCode);
        playListEntryGSL.keyTyped(typedChar, keyCode);
        songGSL.keyTyped(typedChar, keyCode);
        tagGSL.keyTyped(typedChar, keyCode);
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
        userGSL.handleMouseInput(mouseX, mouseY);
        playListGSL.handleMouseInput(mouseX, mouseY);
        playListEntryGSL.handleMouseInput(mouseX, mouseY);
        songGSL.handleMouseInput(mouseX, mouseY);
        tagGSL.handleMouseInput(mouseX, mouseY);
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
