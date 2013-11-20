package net.scholagest.app.rest.object;


public class RestSetPassword extends RestBaseRequest {
    private String teacherKey;
    private String password;

    public String getTeacherKey() {
        return teacherKey;
    }

    public String getNewPassword() {
        return password;
    }
}
