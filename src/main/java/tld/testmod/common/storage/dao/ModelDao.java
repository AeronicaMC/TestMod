package tld.testmod.common.storage.dao;

import com.iciql.Dao;
import tld.testmod.common.storage.models.*;

import java.util.UUID;

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

    @SqlQuery("select * from User order by userName")
    User[] getAllUsers();

    @SqlQuery("select * from PlayList where uidOwner = :uid order by playListName")
    PlayList[] getPlayLists(@Bind("uid") UUID uid);

    @SqlQuery("select * from PlayListEntry where pid = :pid")
    PlayListEntry[] getPlayListEntries(@Bind("pid") long pid);

    @SqlQuery("select * from Song where uidOwner = :uid order by songTitle")
    Song[] getSongs(@Bind("uid") UUID uid);

    @SqlQuery("select * from Tag where uidOwner = :uid order by tagName")
    Tag[] getTags(@Bind("uid") UUID uid);

    @SqlStatement("update Tag set tagName = :name where tid =:id")
    boolean renameTagName(@Bind("id") long id, @Bind("name") String name);

    @SqlStatement("update Tag set tagTitle = :title where tid =:id")
    boolean renameTagTitle(@Bind("id") long id, @Bind("title") String title);

    @SqlStatement("update User set userName = :name where uid =:id")
    boolean updateUserName(@Bind("id") long id, @Bind("name") String name);
}
