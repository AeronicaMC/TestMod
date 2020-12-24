package tld.testmod.common.storage.models;

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
public class Song
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
}
