package tld.testmod.common.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import org.h2.tools.RunScript;
import tld.testmod.ModLogger;
import tld.testmod.common.utils.ModRuntimeException;
import tld.testmod.common.utils.NBTHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static tld.testmod.common.storage.FileHelper.*;

public class ServerDataManager
{
    private static final String SERVER_ID_FILE = "server_id" + FileHelper.EXTENSION_DAT;
    private static final String FORMAT_UNABLE_TO_CREATE = "Unable to create folder: %s and/or file: %s";
    private static final String SERVER_ID_FILE_ERROR = "Delete the <world save>/mxtune/server_id" + FileHelper.EXTENSION_DAT + " file, then try loading the world again.";
    private static UUID serverID;

    private ServerDataManager() { /* NOP */ }

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
            } catch (IOException e)
            {
                ModLogger.error("The %s/%s file could not be written.", SERVER_FOLDER, SERVER_ID_FILE);
                throw new ModRuntimeException(SERVER_ID_FILE_ERROR, e);
            }
        }
    }

    private static void startH2()
    {
        if (!dbCreateIfNotExists())
            throw new ModRuntimeException("Unable to create database");
    }

    private static void stopH2()
    {
        ModLogger.info("***** H2: Stopping");

    }

    //TODO: MUST add SERVER ID to Path! ./modname/server/SERVERID/h2db/filename.mv.db
    private static boolean dbCreateIfNotExists()
    {
        boolean status = false;
        Path path = getLocalDirectory(SERVER_H2DB_FOLDER);
        Path dbFile = path.resolve(SERVER_H2DB_FILENAME);
        ModLogger.info("***** H2: SERVER_H2DB_URL:       %s", SERVER_H2DB_URL);
        ModLogger.info("***** H2: SERVER_H2DB_FOLDER:    %s", path);
        ModLogger.info("***** H2: getDefaultSqlScript(): %s", getDefaultSqlScript());
        if (!dbFile.toFile().exists())
        {
            ModLogger.info("Attempting to create database");
            try
            {
                createDatabase(SERVER_H2DB_URL, "owner", "p455w0rd", getDefaultSqlScript());
                ModLogger.info("***** H2: Created database %s", SERVER_H2DB_FOLDER, SERVER_H2DB_NAME);
                status = true;
            } catch (IOException | SQLException e)
            {
                e.printStackTrace();
                ModLogger.error("***** H2: Unable to create database ./%s./%s", SERVER_H2DB_FOLDER, SERVER_H2DB_NAME);
                if (dbFile.toFile().exists())
                {
                    if (dbFile.toFile().delete())
                        ModLogger.info("***** H2: Deleted database file ./%s./%s", SERVER_H2DB_FOLDER, SERVER_H2DB_NAME);
                    else
                        ModLogger.warning("***** H2: Unable to delete database file ./%s./%s", SERVER_H2DB_FOLDER, SERVER_H2DB_NAME);
                }
            }
        }
        else
        {
            ModLogger.info("***** H2: %s exists!", SERVER_H2DB_FILENAME);
            status = true;
        }
        return status;
    }

    private static void createDatabase(String connUrl, String user, String password, URL url) throws SQLException, IOException
    {
        InputStream in = url.openStream();
        Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        ResultSet rs;

        try (Connection conn = DriverManager.getConnection(connUrl, user, password))
        {
            rs = RunScript.execute(conn, reader);
        }
        if (rs != null && rs.wasNull())
            ModLogger.error("***** H2: Connection or script failure during database creation *****");
    }
}
