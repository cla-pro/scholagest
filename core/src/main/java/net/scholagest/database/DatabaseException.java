package net.scholagest.database;

@SuppressWarnings("serial")
public class DatabaseException extends RuntimeException {
    private int moduleCode = -1;
    private int exceptionCode = -1;

    public DatabaseException(int moduleCode, int exceptionCode, String message) {
        this(moduleCode, exceptionCode, message, null);
    }

    public DatabaseException(int moduleCode, int exceptionCode, String message, Throwable throwable) {
        super(message, throwable);
        this.moduleCode = moduleCode;
        this.exceptionCode = exceptionCode;
    }

    public int getModuleCode() {
        return this.moduleCode;
    }

    public int getExceptionCode() {
        return this.exceptionCode;
    }
}
