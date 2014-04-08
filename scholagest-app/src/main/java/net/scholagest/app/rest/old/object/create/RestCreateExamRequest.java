package net.scholagest.app.rest.old.object.create;

import java.util.List;

import net.scholagest.app.rest.old.object.RestBaseRequest;

public class RestCreateExamRequest extends RestBaseRequest {
    private String yearKey;
    private String classKey;
    private String branchKey;
    private String periodKey;
    private List<String> keys;
    private List<String> values;

    public String getYearKey() {
        return yearKey;
    }

    public String getClassKey() {
        return classKey;
    }

    public String getBranchKey() {
        return branchKey;
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getValues() {
        return values;
    }
}
