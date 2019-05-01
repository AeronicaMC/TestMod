
package tld.testmod.client.gui.util;

import java.util.List;

public interface IHooverText
{
    void addHooverTexts(String hooverText);

    boolean isMouseOverElement(int guiLeft, int guiTop, int mouseX, int mouseY);

    List<String> getHooverTexts();
}
