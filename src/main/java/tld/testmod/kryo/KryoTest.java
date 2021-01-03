
package tld.testmod.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tld.testmod.common.storage.models.PlayList;
import tld.testmod.common.storage.models.User;
import tld.testmod.common.utils.GUID;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class KryoTest
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);

    private static long dLongSigBits;
    private static long cLongSigBits;
    private static long bLongSigBits;
    private static long aLongSigBits;

    private KryoTest() { /* NOP */ }


    public static void main(String[] args) throws Exception
    {
        LOGGER.info("Test Kryo");
        Kryo kryo = new Kryo();
        kryo.register(SomeClass.class);
        kryo.register(User.class);
        kryo.register(PlayList.class);

        // SomeClass - default/Java
        SomeClass object = new SomeClass();
        object.value = "Hello Kryo!";
        LOGGER.info("Output SomeClass: {}", object.value);

        Output output = new Output(new FileOutputStream("logs/file.bin"));
        kryo.writeObject(output, object);
        output.close();

        Input input = new Input(new FileInputStream("logs/file.bin"));
        SomeClass object2 = kryo.readObject(input, SomeClass.class);
        input.close();
        LOGGER.info("Input SomeClass: {}", object2.value);

        // User.class implements KryoSerializable
        User user = new User();
        user.uid = UUID.randomUUID();
        user.userName = "Kryo-san";
        LOGGER.info("Output User: {}, {}", user.userName, user.uid);

        output = new Output(new FileOutputStream("logs/file2.bin"));
        kryo.writeObject(output, user);
        output.close();

        input = new Input(new FileInputStream("logs/file2.bin"));
        User user2 = kryo.readObject(input, User.class);
        input.close();
        LOGGER.info("Input User: {}, {}", user2.userName, user2.uid);

        // PlayList.class implements KryoSerializable
        PlayList playList = new PlayList();
        playList.pid = 1435L;
        playList.uidOwner = UUID.fromString(user2.uid.toString());
        playList.playListName = "Kryo Tunes";
        LOGGER.info("Output PlayList: {}, {}, {}", playList.pid, playList.playListName, playList.uidOwner);

        output = new Output(new FileOutputStream("logs/file3.bin"));
        kryo.writeObject(output, playList);
        output.close();

        input = new Input(new FileInputStream("logs/file3.bin"));
        PlayList playList2 = kryo.readObject(input, PlayList.class);
        input.close();
        LOGGER.info("Input PlayList: {}, {}, {}", playList2.pid, playList2.playListName, playList2.uidOwner);
        LOGGER.info("Long.MAX_VALUE + 1 = {}, Long.MIN_VALUE = {}" ,Long.MAX_VALUE + 1L, Long.MIN_VALUE);
        LOGGER.info("Long.MIN_VALUE - 1 = {}, Long.MAX_VALUE = {}" ,Long.MIN_VALUE - 1L, Long.MAX_VALUE);
    }

    static public class SomeClass {
        String value;
    }
}
