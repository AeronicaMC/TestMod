package tld.testmod.common.storage.models;

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
public class Tag
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
}
