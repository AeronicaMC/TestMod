/*
 * Aeronica's mxTune MOD
 * Copyright 2019, Paul Boese a.k.a. Aeronica
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

/*
 * Borrowed from vazkii.minetunes.config CacheHelper class
 * Why Vazkii did you use the [Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License] on minetunes?
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 */

package tld.testmod.common.storage;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.common.utils.ModRuntimeException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;

public class FileHelper
{
    public static final String MOD_FOLDER = Main.MOD_ID;
    private static final String CLIENT_FOLDER = MOD_FOLDER;
    public static final String CLIENT_MML_FOLDER = CLIENT_FOLDER + "/import_folder";
    public static final String CLIENT_LIB_FOLDER = CLIENT_FOLDER + "/library";
    public static final String CLIENT_SERVER_CACHE_FOLDER = CLIENT_FOLDER + "/server_cache";
    public static final String SERVER_FOLDER = MOD_FOLDER;
    public static final String SERVER_PLAY_LISTS_FOLDER = SERVER_FOLDER + "/playlists";
    public static final String SERVER_MUSIC_FOLDER = SERVER_FOLDER + "/music";

    public static final String EXTENSION_DAT = ".dat";
    public static final String EXTENSION_MXT = ".mxt";

    private static Path serverWorldFolder;

    /**
     * Stores the path of the server side 'world' folder. This changes based on the chosen save folder for SP/LAN
     * integrated server or the 'level-name' property in the server.properties for dedicated servers.
     * <p></p>
     * This needs to be called from the {@link FMLServerStartingEvent}
     */
    public static void initServerStorage()
    {
        // The top level "world" save folder a.k.a. the "Over World"
        WorldServer worldServer = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        File chunkDir = worldServer.getChunkSaveLocation();
        serverWorldFolder = Paths.get(chunkDir.getPath());
    }

    static Path getServerWorldFolder()
    {
        return serverWorldFolder;
    }

    private FileHelper() { /* NOP */ }

    public static PathMatcher getMmlMatcher(Path path)
    {
        return path.getFileSystem().getPathMatcher("glob:**.{mml,ms2mml,zip}");
    }

    public static PathMatcher getDatMatcher(Path path)
    {
        return path.getFileSystem().getPathMatcher("glob:**.{dat}");
    }

    /**
     * Match the .mxt file extension
     * @param path to match files
     * @return files with the mxTune file type extension
     */
    public static PathMatcher getMxtMatcher(Path path)
    {
        return path.getFileSystem().getPathMatcher("glob:**.{mxt}");
    }

    public static String removeExtension(String s)
    {
        return s.replaceAll("(\\.\\w+$)", "");
    }

    public static String normalizeFilename(String s)
    {
        return s.replaceAll("([\\x00-\\x1F!\"\\$\'\\.\\(\\)\\*,\\/:;<>\\?\\[\\\\\\]\\{\\|\\}\\x7F]+)", "");
    }

    public static URL getDefaultSqlScript()
    {
        URL resource = Main.class.getResource("/assets/" + ServerDataManager.DEFAULT_SQL.getNamespace() + "/" + ServerDataManager.DEFAULT_SQL.getPath());
        ModLogger.info("default.sql path: %s", resource);
        return resource;
    }

    private static void fixDirectory(Path dir)
    {
        if (dir.toFile().exists() && !dir.toFile().isDirectory())
            try
            {
                Files.delete(dir);
                Files.createDirectories(dir);
            } catch (IOException e)
            {
                ModLogger.error(e);
                ModLogger.warning("Unable to recreate folder, it exists but is not a directory: %s", (Object) dir);
            }
        else
            try
            {
                Files.createDirectories(dir);

            } catch (IOException e)
            {
                ModLogger.error(e);
                ModLogger.warning("Unable to create folder: %s", (Object) dir);
            }
    }

    /**
     * Open an OS folder on the client side.
     * @param folder of interest
     */
    @SideOnly(Side.CLIENT)
    public static void openFolder(String folder)
    {
        OpenGlHelper.openFile(getDirectory(folder, Side.CLIENT).toFile());
    }

    /**
     * getDirectory is side sensitive.
     * Client side references the game run folder (e.g. .minecraft): "."
     * Server side references the world save folder (e.g. world):  "<world folder name>"
     * This method will create the specified directory if it does not exist!
     * @param folder folder name
     * @param side Side.SERVER or Side CLIENT
     * @return the Path of the folder on the specified side
     */
    public static Path getDirectory(String folder, Side side)
    {
        return getDirectory(folder, side, true);
    }

    /**
     * getDirectory is side sensitive.
     * Client side references the game run folder (e.g. .minecraft): "."
     * Server side references the world save folder (e.g. world):  "<world folder name>"
     * This method will create the specified directory if it does not exist!
     * @param folder folder name
     * @param side Side.SERVER or Side CLIENT
     * @param fixDirectory Recreates the directory if true
     * @return the Path of the folder on the specified side
     */
    public static Path getDirectory(String folder, Side side, boolean fixDirectory)
    {
        String sidedPath = side == Side.SERVER ? serverWorldFolder.toString() : ".";
        Path loc = Paths.get(sidedPath, folder);
        if (fixDirectory)
            fixDirectory(loc);
        return loc;
    }

    public static Path getLocalDirectory(String folder){
        return getLocalDirectory(folder, true);
    }

    public static Path getLocalDirectory(String folder, boolean fixDirectory){
        Path loc = Paths.get(".", folder);
        if (fixDirectory)
            fixDirectory(loc);
        return loc;
    }

    public static Path getCacheFile(String folder, String filename, Side sideIn) throws IOException
    {
        Path dir = getDirectory(folder, sideIn);
        Path cacheFile = dir.resolve(filename);

        if(!cacheFile.toFile().exists())
        {
            Files.createDirectories(dir);
            Files.createFile(cacheFile);
        }
        return cacheFile;
    }

    public static boolean fileExists(String folder, String filename, Side sideIn)
    {
        Path dir = getDirectory(folder, sideIn);
        Path resolve = dir.resolve(filename);
        return resolve.toFile().exists();
    }

    public static NBTTagCompound getCompoundFromFile(Path path)
    {
        if (path == null)
            throw new ModRuntimeException("Missing cache file!");

        try
        {
            return CompressedStreamTools.readCompressed(new FileInputStream(path.toFile()));
        }
        catch (IOException e0)
        {
            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            try
            {
                CompressedStreamTools.writeCompressed(nbtTagCompound, new FileOutputStream(path.toFile()));
                return getCompoundFromFile(path);
            } catch (IOException e1)
            {
                ModLogger.error(e1);
                return null;
            }
        }
    }

    public static void sendCompoundToFile(Path path, NBTTagCompound tagCompound)
    {
        try
        {
            CompressedStreamTools.writeCompressed(tagCompound, new FileOutputStream(path.toFile()));
        } catch(IOException e) {
            ModLogger.error(e);
        }
    }
}
