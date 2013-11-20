package net.scholagest.app.rest.object;

import java.util.Set;

public class RestGetStudentGradesRequest extends RestBaseRequest {
    private Set<String> studentKeys;
    private Set<String> examKeys;
    private String yearKey;

    public Set<String> getStudentKeys() {
        return studentKeys;
    }

    public Set<String> getExamKeys() {
        return examKeys;
    }

    public String getYearKey() {
        return yearKey;
    }
}
