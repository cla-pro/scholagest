package net.scholagest.exception;

public class ScholagestException extends Exception {
    private static final long serialVersionUID = 1862695334395985905L;

    private final ScholagestExceptionErrorCode errorCode;

    public ScholagestException(ScholagestExceptionErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public ScholagestException(ScholagestExceptionErrorCode errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public ScholagestExceptionErrorCode getErrorCode() {
        return errorCode;
    }
}
