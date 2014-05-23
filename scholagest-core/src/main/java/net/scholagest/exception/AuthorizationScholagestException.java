package net.scholagest.exception;

/**
 * Authentication exception that will be caught in the mapper to be transformed into the proper HTTP status code
 * 
 * @author CLA
 * @since 0.17.0
 */
public class AuthorizationScholagestException extends ScholagestRuntimeException {
    private static final long serialVersionUID = 2120035270106794735L;

    public AuthorizationScholagestException(final ScholagestExceptionErrorCode errorCode, final String message) {
        super(errorCode, message);
    }

    public AuthorizationScholagestException(final ScholagestExceptionErrorCode errorCode, final String message, final Throwable throwable) {
        super(errorCode, message, throwable);
    }
}
