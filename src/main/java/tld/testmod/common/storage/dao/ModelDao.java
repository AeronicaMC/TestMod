package tld.testmod.common.storage.dao;

import com.iciql.Dao;
import tld.testmod.common.storage.models.*;

public interface ModelDao extends Dao
{
    @SqlQuery("select * from PlayList")
    PlayList[] getAllPlayLists();

    @SqlQuery("select * from PlayListEntry")
    PlayListEntry[] getAllPlayListEntries();

    @SqlQuery("select * from Song")
    Song[] getAllSongs();

    @SqlQuery("select * from Tag")
    Tag[] getAllTags();

    @SqlQuery("select * from User")
    User[] getAllUsers();

    @SqlStatement("update Tag set tagName = :name where tid =:id")
    boolean renameTagName(@Bind("id") long id, @Bind("name") String name);

    @SqlStatement("update Tag set tagTitle = :title where tid =:id")
    boolean renameTagTitle(@Bind("id") long id, @Bind("title") String title);
}
