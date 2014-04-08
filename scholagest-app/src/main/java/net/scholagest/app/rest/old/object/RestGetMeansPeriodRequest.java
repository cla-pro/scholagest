package net.scholagest.app.rest.old.object;

import java.util.Set;

public class RestGetMeansPeriodRequest extends RestBaseRequest {
    private String periodKey;
    private Set<String> studentKeys;

    public String getPeriodKey() {
        return periodKey;
    }

    public Set<String> getStudentKeys() {
        return studentKeys;
    }
}
