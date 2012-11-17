package net.scholagest.exception;

public class ScholagestException extends Exception {
    private static final long serialVersionUID = 1862695334395985905L;

    private int moduleCode = -1;
    private int exceptionCode = -1;

    public ScholagestException(int moduleCode, int exceptionCode, String message) {
        this(moduleCode, exceptionCode, message, null);
    }

    public ScholagestException(int moduleCode, int exceptionCode, String message, Throwable throwable) {
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
