package tld.testmod.common.storage.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.iciql.Iciql;

import java.util.UUID;

@Iciql.IQTable
@Iciql.IQIndexes({
        @Iciql.IQIndex("pid"),
        @Iciql.IQIndex(name = "uidOwnerIndex", value = "uidOwner")
})
@Iciql.IQContraintForeignKey(
        foreignColumns = {"uidOwner"},
        referenceName = "User",
        referenceColumns = {"uid"},
        deleteType = Iciql.ConstraintDeleteType.CASCADE,
        updateType = Iciql.ConstraintUpdateType.CASCADE
)
public class PlayList implements KryoSerializable
{
    @Iciql.IQColumn(primaryKey = true, autoIncrement = true)
    public Long pid;

    @Iciql.IQColumn
    public UUID uidOwner;

    // String lengths 2x to deal with SQL character escaping
    @Iciql.IQColumn()
    public String playListName;

    public PlayList()
    {
        // default constructor required by iciql api
    }

    @Override
    public void write(Kryo kryo, Output output)
    {
        output.writeVarLong((pid != null) ? pid : 0L, true);
        output.writeString(uidOwner.toString());
        output.writeString(playListName);
        System.out.println("PlayList.write");
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        long pidTemp = input.readVarLong(true);
        pid = (pidTemp != 0L) ? pidTemp : null;
        uidOwner = UUID.fromString(input.readString());
        playListName = input.readString();
        System.out.println("PlayList.read");
    }
}
