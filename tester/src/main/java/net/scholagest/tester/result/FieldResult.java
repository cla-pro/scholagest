package net.scholagest.tester.result;

public class FieldResult {
    private String path;
    private String expectedValue;
    private String receivedValue;
    private FieldResultStatus status;

    public String getExpectedValue() {
        return expectedValue;
    }

    public void setExpectedValue(String expectedValue) {
        this.expectedValue = expectedValue;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getReceivedValue() {
        return receivedValue;
    }

    public void setReceivedValue(String receivedValue) {
        this.receivedValue = receivedValue;
    }

    public FieldResultStatus getStatus() {
        return status;
    }

    public void setStatus(FieldResultStatus status) {
        this.status = status;
    }
}
