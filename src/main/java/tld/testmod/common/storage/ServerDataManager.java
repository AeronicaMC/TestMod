package tld.testmod.common.storage;

import com.iciql.Dao;
import com.iciql.Db;
import com.iciql.util.Utils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import org.h2.tools.RunScript;
import tld.testmod.Main;
import tld.testmod.ModLogger;
import tld.testmod.common.storage.dao.ModelDao;
import tld.testmod.common.storage.models.User;
import tld.testmod.common.utils.ModRuntimeException;
import tld.testmod.common.utils.NBTHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static tld.testmod.common.storage.FileHelper.*;

public class ServerDataManager
{
    static final String SERVER_H2DB_FILENAME = "data.mv.db";
    static final String SERVER_H2DB_NAME = "data";
    private static final String SERVER_ID_FILE = "server_id" + FileHelper.EXTENSION_DAT;
    private static final String FORMAT_UNABLE_TO_CREATE = "Unable to create folder: %s and/or file: %s";
    private static final String SERVER_ID_FILE_ERROR = "Delete the <world save>/mxtune/server_id" + FileHelper.EXTENSION_DAT + " file, then try loading the world again.";
    static final ResourceLocation DEFAULT_SQL = new ResourceLocation(Main.MOD_ID, "db/default.sql");
    private static UUID serverID;
    private static Connection connection = null;
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    private static String dbH2DbFolder;
    private static String dbH2DbUseDbUrl;
    private static String dbH2DbCreateDbUrl;

    private ServerDataManager() { /* NOP */ }

    public static void start()
    {
        getOrGenerateServerID();
        setupDbUrl();
        startH2();
        TestConnection();
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

    private static void setupDbUrl()
    {
        Path loc = Paths.get(getServerWorldFolder().toString(), MOD_FOLDER, "h2db");
        dbH2DbFolder = loc.toString();
        dbH2DbUseDbUrl = "jdbc:h2:async:" + dbH2DbFolder + "/" + SERVER_H2DB_NAME + ";IFEXISTS=TRUE";
        dbH2DbCreateDbUrl = "jdbc:h2:async:" + dbH2DbFolder + "/" + SERVER_H2DB_NAME;
    }

    private static String getDbH2DbFolder()
    {
        return dbH2DbFolder;
    }

    private static String getDbH2DbUseDbUrl()
    {
        return dbH2DbUseDbUrl;
    }
    private static String getDbH2DbCreateDbUrl()
    {
        return dbH2DbCreateDbUrl;
    }

    private static void setupHikariCPConfig()
    {
        config.setJdbcUrl(getDbH2DbUseDbUrl());
        config.setUsername("owner");
        config.setPassword("p455w0rd");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    private static void startH2()
    {
        setupHikariCPConfig();
        if (!dbCreateIfNotExists())
        {
            throw new ModRuntimeException("Unable to create database");
        }
        try
        {
            connection = DriverManager.getConnection(getDbH2DbUseDbUrl(), "owner", "p455w0rd");
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        ds = new HikariDataSource(config);
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }


    private static void TestConnection()
    {
        try (Db db = Db.open(getConnection()))
        {
            ModelDao dao = db.open(ModelDao.class);
            for (User u : dao.getAllUsers())
            {
                ModLogger.info("***** H2: %s, %s", u.userName, u.uid.toString());
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public static void upSertUser(UUID uuid, String name)
    {

        try (Db db = Db.open(getConnection()))
        {
            User user = new User();
            user.uid = uuid;
            user.userName = name;
            db.upsert(user);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

        private static void stopH2()
    {
        ModLogger.info("***** H2: Stopping");
        if (connection != null)
        {
            try
            {
                connection.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static boolean dbCreateIfNotExists()
    {
        boolean status = false;
        Path path = Paths.get(getDbH2DbFolder(), "");
        Path dbFile = path.resolve(SERVER_H2DB_FILENAME);
        ModLogger.info("***** H2: getDbH2DbCreateDbUrl(): %s", getDbH2DbCreateDbUrl());
        ModLogger.info("***** H2: getDbH2DbFolder():      %s", path);
        ModLogger.info("***** H2: getDefaultSqlScript():  %s", getDefaultSqlScript());
        ModLogger.info("***** H2: dbFile:                 %s", dbFile);
        if (!dbFile.toFile().exists())
        {
            ModLogger.info("Attempting to create database");
            try
            {
                createDatabase(getDbH2DbCreateDbUrl(), "owner", "p455w0rd", getDefaultSqlScript());
                ModLogger.info("***** H2: Created database %s", getDbH2DbFolder(), SERVER_H2DB_NAME);
                status = true;
            } catch (IOException | SQLException e)
            {
                e.printStackTrace();
                ModLogger.error("***** H2: Unable to create database ./%s./%s", getDbH2DbFolder(), SERVER_H2DB_NAME);
                if (dbFile.toFile().exists())
                {
                    if (dbFile.toFile().delete())
                        ModLogger.info("***** H2: Deleted database file ./%s./%s", getDbH2DbFolder(), SERVER_H2DB_NAME);
                    else
                        ModLogger.warning("***** H2: Unable to delete database file ./%s./%s", getDbH2DbFolder(), SERVER_H2DB_NAME);
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
