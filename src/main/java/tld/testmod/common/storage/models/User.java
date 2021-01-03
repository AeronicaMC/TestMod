package tld.testmod.common.storage.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.iciql.Iciql;

import java.util.UUID;

@Iciql.IQTable
@Iciql.IQIndexes({
        @Iciql.IQIndex({"uid"}),
        @Iciql.IQIndex(name = "nameIndex", value = "userName")
})
public class User implements KryoSerializable
{
    @Iciql.IQColumn(primaryKey = true)
    public UUID uid;

    @Iciql.IQColumn(length = 64)
    public String userName; // 3-16 characters

    public User()
    {
        // default constructor required by iciql api
    }

    @Override
    public void write(Kryo kryo, Output output)
    {
        output.writeString(uid.toString());
        output.writeString(userName);
        System.out.println("User.write");
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        uid = UUID.fromString(input.readString());
        userName = input.readString();
        System.out.println("User.read");
    }
}
