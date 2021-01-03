package tld.testmod.common.storage.models;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.iciql.Iciql;

import java.util.UUID;

@Iciql.IQTable
@Iciql.IQIndexes({
        @Iciql.IQIndex("tid"),
        @Iciql.IQIndex(name = "uidOwnerIndex", value = "uidOwner"),
        @Iciql.IQIndex(name = "pid1Index", value = "pid1"),
        @Iciql.IQIndex(name = "pid2Index", value = "pid2"),
        @Iciql.IQIndex(name = "pid3Index", value = "pid3"),
        @Iciql.IQIndex(name = "tagNameIndex", value = "tagName"),
})
@Iciql.IQContraintForeignKey(
        foreignColumns = {"uidOwner"},
        referenceName = "User",
        referenceColumns = {"uid"},
        deleteType = Iciql.ConstraintDeleteType.CASCADE,
        updateType = Iciql.ConstraintUpdateType.CASCADE
)
public class Tag implements KryoSerializable
{

    @Iciql.IQColumn(primaryKey = true, autoIncrement = true)
    public Long tid;

    @Iciql.IQColumn
    public UUID uidOwner;

    @Iciql.IQColumn
    public Long pid1;

    @Iciql.IQColumn
    public Long pid2;

    @Iciql.IQColumn
    public Long pid3;

    @Iciql.IQColumn(length = 128)
    public String tagName;

    @Iciql.IQColumn()
    public String tagTitle;

    public Tag()
    {
        // default constructor required by iciql api
    }

    @Override
    public void write(Kryo kryo, Output output)
    {
        output.writeVarLong((tid != null) ? tid : 0L, true);
        output.writeString(uidOwner.toString());
        output.writeVarLong((pid1 != null) ? pid1 : 0L, true);
        output.writeVarLong((pid2 != null) ? pid2 : 0L, true);
        output.writeVarLong((pid3 != null) ? pid3 : 0L, true);
        output.writeString(tagName);
        output.writeString(tagTitle);
    }

    @Override
    public void read(Kryo kryo, Input input)
    {
        long tidTemp = input.readVarLong(true);
        tid = (tidTemp != 0L) ? tidTemp : null;
        uidOwner = UUID.fromString(input.readString());
        long pid1Temp = input.readVarLong(true);
        pid1 = (pid1Temp != 0L) ? pid1Temp : null;
        long pid2Temp = input.readVarLong(true);
        pid2 = (pid2Temp != 0L) ? pid2Temp : null;
        long pid3Temp = input.readVarLong(true);
        pid3 = (pid3Temp != 0L) ? pid3Temp : null;
        tagName = input.readString();
        tagTitle = input.readString();
    }
}
