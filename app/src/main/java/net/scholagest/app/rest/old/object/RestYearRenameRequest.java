package net.scholagest.app.rest.old.object;


public class RestYearRenameRequest extends RestBaseRequest {
    private String key;
    private String newYearName;

    public String getKey() {
        return key;
    }

    public String getNewYearName() {
        return newYearName;
    }
}
