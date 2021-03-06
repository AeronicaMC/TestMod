package tld.testmod.common.storage.capability;

import tld.testmod.common.storage.models.*;

import java.util.HashSet;
import java.util.Set;

public class MusicDBImpl implements IMusicDB
{
    private boolean session;
    private PlayList[] playLists = new PlayList[0];
    private final Set<Long> playListIDs = new HashSet<>();
    private PlayListEntry[] playListEntries = new PlayListEntry[0];
    private Song[] songs = new Song[0];
    private final Set<Long> songIDs = new HashSet<>();
    private Tag[] tags = new Tag[0];
    private final Set<Long> tagIDs = new HashSet<>();

    // OP Only
    private User[] users = new User[0];

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
    public PlayListEntry[] getPlayListEntries()
    {
        return playListEntries;
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
    public void setPlayListEntries(PlayListEntry[] playListEntries)
    {
        this.playListEntries = playListEntries;
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

    @Override
    public void resetDbData()
    {
        playLists = new PlayList[0];
        playListIDs.clear();
        playListEntries = new PlayListEntry[0];
        songs = new Song[0];
        songIDs.clear();
        tags = new Tag[0];
        tagIDs.clear();
        users = new User[0];
    }
}
