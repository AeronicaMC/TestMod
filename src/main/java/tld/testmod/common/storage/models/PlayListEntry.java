package tld.testmod.common.storage.models;

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
public class PlayListEntry
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
}
