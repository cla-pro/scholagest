package net.scholagest.exception;

public enum ScholagestExceptionErrorCode {
    GENERAL(0),
    // Authentication and authorization
    USER_NOT_FOUND(1),
    WRONG_PASSWORD(2),
    INSUFFICIENT_PRIVILEGES(3),

    YEAR_ALREADY_RUNNING(1000);

    private int code;

    private ScholagestExceptionErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
