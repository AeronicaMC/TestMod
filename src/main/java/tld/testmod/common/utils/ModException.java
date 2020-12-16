package tld.testmod.common.utils;

public class ModException extends Exception
{

    private static final long serialVersionUID = 5577737375634119060L;

    public ModException() {/* empty by design */}

    public ModException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ModException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ModException(String message)
    {
        super(message);
    }

    public ModException(Throwable cause)
    {
        super(cause);
    }

}
