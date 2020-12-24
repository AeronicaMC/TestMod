package tld.testmod.common.storage.models;

import com.iciql.Iciql;

import java.util.UUID;

@Iciql.IQTable
@Iciql.IQIndexes({
        @Iciql.IQIndex({"uid"}),
        @Iciql.IQIndex(name = "nameIndex", value = "userName")
})
public class User
{
    @Iciql.IQColumn(primaryKey = true)
    public UUID uid;

    @Iciql.IQColumn(length = 64)
    public String userName; // 3-16 characters

    public User()
    {
        // default constructor required by iciql api
    }
}
