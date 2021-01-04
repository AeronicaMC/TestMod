package tld.testmod.common.storage.capability;

import tld.testmod.common.storage.models.PlayList;
import tld.testmod.common.storage.models.Song;
import tld.testmod.common.storage.models.Tag;
import tld.testmod.common.storage.models.User;

import java.util.HashSet;
import java.util.Set;

public class MusicDBImpl implements IMusicDB
{
    private boolean session;
    private PlayList[] playLists = new PlayList[0];
    private final Set<Long> playListIDs = new HashSet<>();
    private Song[] songs = new Song[0];
    private final Set<Long> songIDs = new HashSet<>();
    private Tag[] tags = new Tag[0];
    private final Set<Long> tagIDs = new HashSet<>();

    // OP Only
    private User[] users;

    MusicDBImpl() { /* NOP */}

    @Override
    public boolean isSessionOpen()
    {
        return session;
    }

    @Override
    public void closeSession()
    {
        session = false;
    }

    @Override
    public void openSession()
    {
        session = true;
    }

    @Override
    public PlayList[] getPlaylists()
    {
        return playLists;
    }

    @Override
    public Song[] getSongs()
    {
        return songs;
    }

    @Override
    public Tag[] getTags()
    {
        return tags;
    }

    @Override
    public User[] getUsers()
    {
        return users;
    }

    @Override
    public void setPlaylists(PlayList[] playlists)
    {
        this.playLists = playlists;
    }

    @Override
    public void setSongs(Song[] songs)
    {
        this.songs = songs;
    }

    @Override
    public void setTags(Tag[] tags)
    {
        this.tags = tags;
    }

    @Override
    public void setUsers(User[] users)
    {
        this.users = users;
    }

    @Override
    public Set<Long> getPlayListIDs()
    {
        return playListIDs;
    }

    @Override
    public Set<Long> getSongIDs()
    {
        return songIDs;
    }

    @Override
    public Set<Long> getTagTDs()
    {
        return tagIDs;
    }
}
