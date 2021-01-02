package tld.testmod.common.storage.capability;

public interface IMusicDB
{
    boolean isSessionOpen();

    void closeSession();

    void openSession();
}
