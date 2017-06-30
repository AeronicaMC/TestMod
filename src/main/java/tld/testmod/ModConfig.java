package tld.testmod;

/*
    Licenses for assets can be found in ASSETS_LICENSE.txt. All other files are licensed under the terms below:
    
    The MIT License (MIT)
    
    Test Mod 3 - Copyright (c) 2015-2017 Choonster
    
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
    
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
    
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
 */
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Main.MODID)
@Config.LangKey("testmod.config.title")
public class ModConfig {

    @Config.Comment("This is an example boolean property.")
    public static boolean fooBar = false;

    public static final Client client = new Client();

    public static class Client {

        @Config.Comment("This is an example int property.")
        public int baz = -100;

        public final HUDPos chunkEnergyHUDPos = new HUDPos(0, 0);

        public static class HUDPos {
            public HUDPos(final int x, final int y) {
                this.x = x;
                this.y = y;
            }

            @Config.Comment("The x coordinate")
            public int x;

            @Config.Comment("The y coordinate")
            public int y;
        }
    }

    @Mod.EventBusSubscriber
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(Main.MODID)) {
                ConfigManager.sync(Main.MODID, Config.Type.INSTANCE);
            }
        }
    }
}