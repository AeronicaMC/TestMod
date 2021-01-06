package tld.testmod.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public abstract class AbstractClickableElement
{
    protected Minecraft mc;
    protected GuiScreen gui;
    protected int elementWidth;
    protected int elementHeight;
    protected int slotHeight;
    protected int screenWidth;
    protected int screenHeight;
    protected int top;
    protected int bottom;
    protected int right;
    protected int left;
    protected int guiLeft = 0;
    protected int guiTop = 0;
    protected int mouseX;
    protected int mouseY;
    protected int selectedIndex = -1;
    private long lastClickTime = 0L;
    private float initialMouseClickY = -2.0F;
    protected float scrollDistance = 0;
    protected boolean highlightSelected = false;

    public AbstractClickableElement(GuiScreen gui)
    {
        this.gui = gui;
    }

    public AbstractClickableElement(Minecraft mc, int width, int height, int top, int bottom, int left, int screenWidth, int screenHeight)
    {
        this.mc = mc;
        this.elementWidth = width;
        this.elementHeight = height;
        this.slotHeight = height;
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = width + this.left;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void setLayout(int entryHeight, int width, int height, int top, int left)
    {
        this.elementWidth = width;
        this.elementHeight = height;
        this.slotHeight = entryHeight;
        this.top = top;
        this.bottom = top + height;
        this.left = left;
        this.right = width + this.left;
        this.screenWidth = gui.width;
        this.screenHeight = gui.height;
    }

    /**
     * Used to the set the guiLeft and guiTop of a guiContainer based gui.
     * @param guiLeft guiLeft from the parent gui.
     * @param guiTop guiTop from the parent gui.
     */
    public void setGui(int guiLeft, int guiTop)
    {
        this.guiLeft = guiLeft;
        this.guiTop = guiTop;
    }

    /**
     * Draw anything special on the screen. GL_SCISSOR is enabled for anything that
     * is rendered outside of the view box. Do not mess with SCISSOR unless you support this.
     */
    protected abstract void drawSlot(int entryRight, int slotTop, int slotBuffer, Tessellator tess);

    protected int getContentHeight()
    {
        return this.slotHeight;
    }

    protected void setHighlightSelected(boolean state)
    {
        this.highlightSelected = state;
    }

    public int getRight() {return right;}

    public int getLeft() {return left;}

    public int getTop() {return top;}

    public int getBottom() {return bottom;}

    protected abstract void elementClicked(int index, boolean doubleClick);

    public boolean isSelected(int index)
    {
        return true;
    }

    public boolean isHovering()
    {
        return mouseX >= left && mouseX <= left + elementWidth && mouseY >= top && mouseY <= bottom;
    }

    public void handleMouseInput(int mouseX, int mouseY) throws IOException
    {
        boolean isHovering = mouseX >= this.left && mouseX <= this.left + this.elementWidth &&
                mouseY >= this.top && mouseY <= this.bottom;
    }

    protected abstract void drawBackground();

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        if (bottom - top < 0) return;

        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.drawBackground();

        int listLength     = 1;
        int entryLeft      = this.left + 3;
        int entryRight     = this.left + this.elementWidth - 3;
        int viewHeight     = this.bottom - this.top;
        int border         = 4;

        if (Mouse.isButtonDown(0))
        {
            if (this.initialMouseClickY == -1.0F)
            {
                if (isHovering())
                {
                    int mouseListY = mouseY - this.top - border;
                    int slotIndex = 0;

                    if (mouseX >= entryLeft && mouseX <= entryRight && slotIndex >= 0 && mouseListY >= 0 && slotIndex < listLength)
                    {
                        this.elementClicked(slotIndex, slotIndex == this.selectedIndex && System.currentTimeMillis() - this.lastClickTime < 250L);
                        this.selectedIndex = slotIndex;
                        this.highlightSelected = true;
                        this.lastClickTime = System.currentTimeMillis();
                    }
                    this.initialMouseClickY = mouseY;
                }
                else
                {
                    this.initialMouseClickY = -2.0F;
                }
            }
            else if (this.initialMouseClickY >= 0.0F)
            {
                this.initialMouseClickY = (float)mouseY;
            }
        }
        else
        {
            this.initialMouseClickY = -1.0F;
        }

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder worldr = tess.getBuffer();

        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = mc.displayWidth / res.getScaledWidth_double();
        double scaleH = mc.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)(left      * scaleW), (int)(mc.displayHeight - (bottom * scaleH)),
                       (int)(elementWidth * scaleW), (int)(viewHeight * scaleH));

//        if (this.mc.world != null)
//        {
//            this.drawGradientRect(this.left, this.top, this.right, this.bottom, 0xC0101010, 0xD0101010);
//        }
//        else // Draw dark dirt background
//        {
//            GlStateManager.disableLighting();
//            GlStateManager.disableFog();
//            this.mc.renderEngine.bindTexture(Gui.OPTIONS_BACKGROUND);
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            final float scale = 32.0F;
//            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
//            worldr.pos(this.left,  this.bottom, 0.0D).tex(this.left  / scale, (this.bottom + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.pos(this.right, this.bottom, 0.0D).tex(this.right / scale, (this.bottom + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.pos(this.right, this.top,    0.0D).tex(this.right / scale, (this.top    + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            worldr.pos(this.left,  this.top,    0.0D).tex(this.left  / scale, (this.top    + (int)this.scrollDistance) / scale).color(0x20, 0x20, 0x20, 0xFF).endVertex();
//            tess.draw();
//        }
        int slotIdx = 0;
        int baseY = this.top + border;
        int slotTop = baseY + slotIdx * this.slotHeight;
        int slotBuffer = this.slotHeight - border;

        // Consider horizontal and vertical tab group: tabNames ...

        if (this.highlightSelected)
        {
            int min = this.left;
            int max = entryRight;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.disableTexture2D();
            worldr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
            worldr.pos(min,     slotTop + slotBuffer + (double) 2, 0).tex(0, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(max,     slotTop + slotBuffer + (double) 2, 0).tex(1, 1).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(max,     slotTop              - (double) 2, 0).tex(1, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(min,     slotTop              - (double) 2, 0).tex(0, 0).color(0x80, 0x80, 0x80, 0xFF).endVertex();
            worldr.pos(min + (double) 1, slotTop + slotBuffer + (double) 1, 0).tex(0, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(max - (double) 1, slotTop + slotBuffer + (double) 1, 0).tex(1, 1).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(max - (double) 1, slotTop              - (double) 1, 0).tex(1, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            worldr.pos(min + (double) 1, slotTop              - (double) 1, 0).tex(0, 0).color(0x00, 0x00, 0x00, 0xFF).endVertex();
            tess.draw();
            GlStateManager.enableTexture2D();
        }
        this.drawSlot(entryRight, slotTop, slotBuffer, tess);

        GlStateManager.disableDepth();
        this.drawScreen(mouseX, mouseY);
        GlStateManager.enableTexture2D();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    protected  void drawScreen(int mouseX, int mouseY)
    {

    }

    protected void drawGradientRect(int left, int top, int right, int bottom, int color1, int color2)
    {
        GuiUtils.drawGradientRect(0, left, top, right, bottom, color1, color2);
    }
}
