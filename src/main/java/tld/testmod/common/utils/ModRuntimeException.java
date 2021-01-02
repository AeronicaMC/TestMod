package tld.testmod.common.utils;

public class ModRuntimeException extends RuntimeException
{
    private static final long serialVersionUID = -7749888847279548143L;
    
    public ModRuntimeException() { super(); }

    public ModRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ModRuntimeException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public ModRuntimeException(String message)
    {
        super(message);
    }

    public ModRuntimeException(Throwable cause)
    {
        super(cause);
    }
}
