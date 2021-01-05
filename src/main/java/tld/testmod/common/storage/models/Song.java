package tld.testmod.common.storage.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.iciql.Iciql;

import java.util.UUID;

@Iciql.IQTable
@Iciql.IQIndexes({
       @Iciql.IQIndex("sid"),
       @Iciql.IQIndex(name = "uidOwnerIndex", value = "uidOwner"),
       @Iciql.IQIndex(name = "songTitleIndex", value = "songTitle")
})
@Iciql.IQContraintForeignKey(
        foreignColumns = {"uidOwner"},
        referenceName = "User",
        referenceColumns = {"uid"},
        deleteType = Iciql.ConstraintDeleteType.CASCADE,
        updateType = Iciql.ConstraintUpdateType.CASCADE
)
public class Song implements KryoSerializable
{
    @Iciql.IQColumn(primaryKey = true, autoIncrement = true)
    public Long sid;

    @Iciql.IQColumn(nullable = false)
    public UUID uidOwner;

    // String lengths 2x to deal with SQL character escaping
    @Iciql.IQColumn(length = 128)
    public String songTitle;

    @Iciql.IQColumn
    public int duration;

    public Song()
    {
        // default constructor required by iciql api
    }

    @Override
    public String toString()
    {
        return "sid: " + sid + ", uid: " + uidOwner.toString() + ", '" + songTitle + "', " + "duration: " + duration;
    }

    @Override
    public void write(Kryo kryo, Output output)
    {
        output.writeVarLong((sid != null) ? sid : 0L, true);
        output.writeLong(uidOwner.getMostSignificantBits());
        output.writeLong(uidOwner.getLeastSignificantBits());
        output.writeString(songTitle);
        output.writeVarInt(duration, true);
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        long sidTemp = input.readVarLong(true);
        sid = (sidTemp != 0L) ? sidTemp : null;
        long msb = input.readLong();
        uidOwner = new UUID(msb, input.readLong());
        songTitle = input.readString();
        duration = input.readVarInt(true);
    }
}
