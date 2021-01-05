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
        output.writeLong(uid.getMostSignificantBits());
        output.writeLong(uid.getLeastSignificantBits());
        output.writeString(userName);
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        long msb = input.readLong();
        uid = new UUID(msb, input.readLong());
        userName = input.readString();
    }
}
