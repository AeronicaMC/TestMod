package tld.testmod.common.world.chunk;

public class ModChunkDataImpl implements IModChunkData
{
    private boolean functional;
    private String string;

    ModChunkDataImpl()
    {
        functional = false;
        string = "";
    }

    @Override
    public boolean isFunctional()
    {
        return functional;
    }

    @Override
    public void setFunctional(boolean functional)
    {
        this.functional = functional;
    }

    @Override
    public String getString()
    {
        return string;
    }

    @Override
    public void setString(String string)
    {
        this.string = string;
    }
}
