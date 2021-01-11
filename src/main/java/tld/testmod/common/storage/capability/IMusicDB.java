package tld.testmod.common.storage.capability;

import scala.collection.$colon$plus;
import tld.testmod.common.storage.models.*;

import java.util.Set;

public interface IMusicDB
{
    // testing basic mechanics
    // Server -> Client
    boolean isSessionOpen();

    void closeSession();

    void openSession();

    PlayList[] getPlaylists();

    PlayListEntry[] getPlayListEntries();

    Song[] getSongs();

    Tag[] getTags();

    User[] getUsers();

    void setPlaylists(PlayList[] playlists);

    void setPlayListEntries(PlayListEntry[] playListEntries);

    void setSongs(Song[] songs);

    void setTags(Tag[] tags);

    void setUsers(User[] users);

    Set<Long> getPlayListIDs();

    Set<Long> getSongIDs();

    Set<Long> getTagTDs();

    void resetDbData();
}
