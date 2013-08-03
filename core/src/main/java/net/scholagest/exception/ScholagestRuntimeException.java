package net.scholagest.exception;

public class ScholagestRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3437787488742779936L;

    private final ScholagestExceptionErrorCode errorCode;

    public ScholagestRuntimeException(ScholagestExceptionErrorCode errorCode, String message) {
        this(errorCode, message, null);
    }

    public ScholagestRuntimeException(ScholagestExceptionErrorCode errorCode, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCode = errorCode;
    }

    public ScholagestExceptionErrorCode getErrorCode() {
        return errorCode;
    }
}
