
package tld.testmod.client.gui.util;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.util.List;

public class ModGuiUtils
{
    public static final ModGuiUtils INSTANCE = new ModGuiUtils(){};

    public static boolean isPointInRegion(int x, int y, int height, int width, int guiLeft,  int guiTop, int pointX, int pointY)
    {
        pointX = pointX - guiLeft;
        pointY = pointY - guiTop;
        int rectX = x - guiLeft;
        int rectY = y - guiTop;
        int rectWidth = width;
        int rectHeight = height;
        return pointX >= rectX - 1 && pointX < rectX + rectWidth + 1 && pointY >= rectY - 1 && pointY < rectY + rectHeight + 1;
    }

    public <T extends GuiScreen, S extends Object>  void drawHooveringHelp(T guiScreen, List<S> hooverTexts, int guiLeft, int guiTop, int mouseX, int mouseY)
    {
        for(Object text : hooverTexts)
            if (text instanceof IHooverText && ((IHooverText) text).isMouseOverElement(guiLeft, guiTop, mouseX, mouseY) && (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)))
                guiScreen.drawHoveringText(((IHooverText) text).getHooverTexts(), mouseX, mouseY);
    }

    public static <T extends GuiTextField> void clearOnMouseLeftClicked(T guiTextField, int mouseX, int mouseY, int mouseButton)
    {
        if (mouseButton == 1 && mouseX >= guiTextField.x && mouseX < guiTextField.x + guiTextField.width
                && mouseY >= guiTextField.y && mouseY < guiTextField.y + guiTextField.height)
        {
            guiTextField.setText("");
        }
    }
}
