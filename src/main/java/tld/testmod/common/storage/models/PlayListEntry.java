package tld.testmod.common.storage.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.iciql.Iciql;

@Iciql.IQTable
@Iciql.IQIndexes({
        @Iciql.IQIndex("eid"),
        @Iciql.IQIndex(name = "pidIndex", value = "pid"),
        @Iciql.IQIndex(name = "sidIndex", value = "sid")
})
@Iciql.IQContraintForeignKey(
        foreignColumns = {"pid"},
        referenceName = "PlayList",
        referenceColumns = {"pid"},
        deleteType = Iciql.ConstraintDeleteType.CASCADE,
        updateType = Iciql.ConstraintUpdateType.CASCADE
)
public class PlayListEntry implements KryoSerializable
{
    @Iciql.IQColumn(primaryKey = true, autoIncrement = true)
    public Long eid;

    @Iciql.IQColumn(nullable = false)
    public Long pid;

    @Iciql.IQColumn(nullable = false)
    public Long sid;

    public PlayListEntry()
    {
        // default constructor required by iciql api
    }

    @Override
    public void write(Kryo kryo, Output output)
    {
        output.writeVarLong((eid != null) ? eid : 0L, true);
        output.writeVarLong((pid != null) ? pid : 0L, true);
        output.writeVarLong((sid != null) ? sid : 0L, true);
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        long eidTemp = input.readVarLong(true);
        eid = (eidTemp != 0L) ? eidTemp : null;
        long pidTemp = input.readVarLong(true);
        pid = (pidTemp != 0L) ? pidTemp : null;
        long sidTemp = input.readVarLong(true);
        sid = (sidTemp != 0L) ? sidTemp : null;
    }
}
