package net.scholagest.app.rest.old.object.create;

import java.util.List;

import net.scholagest.app.rest.old.object.RestBaseRequest;

public class RestCreateTeacherRequest extends RestBaseRequest {
    private String teacherType;
    private List<String> keys;
    private List<String> values;

    public String getTeacherType() {
        return teacherType;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getValues() {
        return values;
    }
}
