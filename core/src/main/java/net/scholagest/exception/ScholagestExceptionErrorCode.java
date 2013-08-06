package net.scholagest.exception;

public enum ScholagestExceptionErrorCode {
    GENERAL(0, 0),
    // Authentication and authorization
    USER_NOT_FOUND(1, 1), WRONG_PASSWORD(2, 1), INSUFFICIENT_PRIVILEGES(3, 3), SESSION_EXPIRED(10, 10), YEAR_ALREADY_RUNNING(1000, 1000), GRADE_NOT_NUMERICAL(
            2000, 2000), INVALID_TEACHER_TYPE(3000, 3000);

    private int code;

    private int publicCode;

    private ScholagestExceptionErrorCode(int code, int publicCode) {
        this.code = code;
        this.publicCode = publicCode;
    }

    public int getCode() {
        return code;
    }

    public int getPublicCode() {
        return publicCode;
    }
}
