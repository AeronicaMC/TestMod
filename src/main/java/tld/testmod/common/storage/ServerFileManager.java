package tld.testmod.common.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import tld.testmod.ModLogger;
import tld.testmod.common.utils.ModRuntimeException;
import tld.testmod.common.utils.NBTHelper;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import static tld.testmod.common.storage.FileHelper.*;

public class ServerFileManager
{
    private static final String SERVER_ID_FILE = "server_id" + FileHelper.EXTENSION_DAT;
    private static final String FORMAT_UNABLE_TO_CREATE = "Unable to create folder: %s and/or file: %s";
    private static final String SERVER_ID_FILE_ERROR = "Delete the <world save>/mxtune/server_id" + FileHelper.EXTENSION_DAT + " file, then try loading the world again.";
    private static UUID serverID;
    private static String h2URL;
    private static Connection connexion;
    
    private ServerFileManager() { /* NOP */ }

    public static void start()
    {
        getOrGenerateServerID();
        startH2();
    }

    public static void shutdown()
    {
        stopH2();
    }

    public static UUID getServerID()
    {
        return serverID;
    }

    private static void getOrGenerateServerID()
    {
        boolean fileExists = FileHelper.fileExists(SERVER_FOLDER, SERVER_ID_FILE, Side.SERVER);
        NBTTagCompound compound;
        Path serverDatFile;
        if (fileExists)
        {
            try
            {
                serverDatFile = FileHelper.getCacheFile(SERVER_FOLDER, SERVER_ID_FILE, Side.SERVER);
                compound = FileHelper.getCompoundFromFile(serverDatFile);
                if (compound != null)
                    serverID = NBTHelper.getUuidFromCompound(compound);
                else throw new NullPointerException("NBTTagCompound compound is null!");
            } catch (NullPointerException | IOException e)
            {
                ModLogger.error("The %s/%s file could not be read.", SERVER_FOLDER, SERVER_ID_FILE);
                throw new ModRuntimeException(SERVER_ID_FILE_ERROR, e);
            }
        }
        else
        {
            try
            {
                serverDatFile = FileHelper.getCacheFile(SERVER_FOLDER, SERVER_ID_FILE, Side.SERVER);
                serverID = UUID.randomUUID();
                compound = new NBTTagCompound();
                NBTHelper.setUuidToCompound(compound, serverID);
                FileHelper.sendCompoundToFile(serverDatFile, compound);
            }
            catch (IOException e)
            {
                ModLogger.error("The %s/%s file could not be written.", SERVER_FOLDER, SERVER_ID_FILE );
                throw new ModRuntimeException(SERVER_ID_FILE_ERROR, e);
            }
        }
    }

    private static void startH2()
    {
        Path dir = FileHelper.getDirectory(SERVER_H2DB_FOLDER, Side.SERVER);
        Path dbFile = dir.resolve(SERVER_H2DB_NAME);
        h2URL = "jdbc:h2:" + dbFile.toString();
        ModLogger.info("***** H2: %s" , h2URL);
        try
        {
            Class.forName("org.h2.Driver").newInstance();
            connexion = DriverManager.getConnection(h2URL, "sa", "");
        } catch (InstantiationException | SQLException | IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            Statement stmt = connexion.createStatement();
            ModLogger.info("Create DB: %s", stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))"));
            stmt = connexion.createStatement();
            ModLogger.info("Create DB: %s", stmt.executeUpdate("INSERT INTO TEST VALUES(1, 'Hello')"));
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private static void stopH2()
    {
        ModLogger.info("***** H2: Stopping");
        try
        {
            if (connexion != null)
                connexion.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

}
