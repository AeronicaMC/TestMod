
package tld.testmod.client.gui.util;

import net.minecraft.client.gui.GuiButton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
public class GuiButtonMX extends GuiButton implements IHooverText
{
    private List<String> hooverTexts = new ArrayList<>();
    private List<String> hooverTextsCopy = new ArrayList<>();

    private String statusText = "";

    public GuiButtonMX(int buttonId, int x, int y, String buttonText)
    {
        super(buttonId, x, y, buttonText);
        if(!buttonText.equals("")) hooverTexts.add(buttonText);
    }

    public GuiButtonMX(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        if(!buttonText.equals("")) hooverTexts.add(buttonText);
    }

    @Override
    public List<String> getHooverTexts()
    {
        hooverTextsCopy.clear();
        hooverTextsCopy.addAll(hooverTexts);
        if (!statusText.equals(""))hooverTextsCopy.add(statusText);
        return hooverTextsCopy;
    }

    public void addHooverTexts(String hooverText)
    {
        hooverTexts.add(hooverText);
    }

    public void setStatusText(String statusText)
    {
        this.statusText = statusText;
    }

    public String getStatusText()
    {
        return statusText;
    }

    @Override
    public boolean isMouseOverElement(int guiLeft, int guiTop, int mouseX, int mouseY)
    {
        return ModGuiUtils.isPointInRegion(x, y, height, width, guiLeft, guiTop, mouseX, mouseY);
    }
}
