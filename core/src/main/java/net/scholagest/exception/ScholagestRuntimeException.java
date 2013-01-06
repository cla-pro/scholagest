package net.scholagest.exception;

public class ScholagestRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 3437787488742779936L;

    private int moduleCode = -1;
    private int exceptionCode = -1;

    public ScholagestRuntimeException(int moduleCode, int exceptionCode, String message) {
        this(moduleCode, exceptionCode, message, null);
    }

    public ScholagestRuntimeException(int moduleCode, int exceptionCode, String message, Throwable throwable) {
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
