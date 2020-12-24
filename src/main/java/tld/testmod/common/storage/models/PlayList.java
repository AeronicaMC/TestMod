package tld.testmod.common.storage.models;

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
public class PlayList
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
}
