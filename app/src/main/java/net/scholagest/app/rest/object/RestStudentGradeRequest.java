package net.scholagest.app.rest.object;

public class RestStudentGradeRequest {
    private String token;
    private RestStudentGradeList object;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RestStudentGradeList getGrades() {
        return object;
    }

    public void setObject(RestStudentGradeList object) {
        this.object = object;
    }
}
