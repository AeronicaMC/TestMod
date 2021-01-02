package tld.testmod.common.storage.capability;

public class MusicDBImpl implements IMusicDB
{
    private boolean session;

    MusicDBImpl()
    {
    }

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
}
