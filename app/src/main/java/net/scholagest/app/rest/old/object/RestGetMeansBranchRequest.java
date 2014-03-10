package net.scholagest.app.rest.old.object;

import java.util.Set;

public class RestGetMeansBranchRequest extends RestBaseRequest {
    private String branchKey;
    private Set<String> studentKeys;

    public String getBranchKey() {
        return branchKey;
    }

    public Set<String> getStudentKeys() {
        return studentKeys;
    }
}
