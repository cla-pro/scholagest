package net.scholagest.app.rest.old.object.create;

import java.util.List;

import net.scholagest.app.rest.old.object.RestBaseRequest;

public class RestCreateStudentRequest extends RestBaseRequest {
    private List<String> keys;
    private List<String> values;

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getValues() {
        return values;
    }
}
