
package tld.testmod.client.gui.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nonnull;

public class GuiLink extends GuiButton
{
    private final AlignText alignText;

    public enum AlignText
    {
        LEFT, CENTER, RIGHT
    }

    public GuiLink(int buttonId, int x, int y, String buttonText, AlignText alignText)
    {
        super(buttonId, x, y, 200, 20, buttonText);
        this.alignText = alignText;
    }

    @SuppressWarnings("unused")
    public GuiLink(int buttonId, int x, int y, String buttonText)
    {
        super(buttonId, x, y, 200, 20, buttonText);
        this.alignText = AlignText.LEFT;
    } 
    
    @Override
    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            ITextComponent formattedLink = ForgeHooks.newChatWithLinks(this.displayString, false);
            int stringWidth = fontrenderer.getStringWidth(formattedLink.getFormattedText());
            int alignX = this.x;
            switch (this.alignText)
            {
            case RIGHT:
                alignX = this.x - stringWidth;
                break;
            case CENTER:
                alignX = this.x - stringWidth + stringWidth / 2;
                break;
            case LEFT:
                break;
            default:
            }
            this.drawString(fontrenderer, formattedLink.getFormattedText(), alignX, this.y, 0xFF0000);
         }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        boolean result = false;
        FontRenderer fontrenderer = mc.fontRenderer;
        ITextComponent formattedLink = ForgeHooks.newChatWithLinks(this.displayString, false);
        int stringWidth = fontrenderer.getStringWidth(formattedLink.getFormattedText());
        int stringHeight = fontrenderer.FONT_HEIGHT;
        if (this.alignText.equals(AlignText.LEFT))
            result =  this.enabled && this.visible && mouseX >= this.x && mouseX < this.x + stringWidth && mouseY >= this.y && mouseY < this.y + stringHeight;
        else if (this.alignText.equals(AlignText.CENTER))
            result =  this.enabled && this.visible && mouseX >= this.x - stringWidth / 2 && mouseX < this.x + stringWidth / 2 && mouseY >= this.y && mouseY < this.y + stringHeight;
        else if (this.alignText.equals(AlignText.RIGHT))
            result =  this.enabled && this.visible && mouseX >= this.x - stringWidth && mouseX < this.x && mouseY >= this.y && mouseY < this.y + stringHeight;
            
        return result;
    }
       
    /**
     * Call this method from the parent class that extends GuiScreen e.g. "this.handleComponentClick(mmlLink.getLinkComponent());"
     * @return ITextComponent for the URL string
     */
    public ITextComponent getLinkComponent()
    {
        return ForgeHooks.newChatWithLinks(this.displayString, false);
    }
}
