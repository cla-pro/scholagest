package net.scholagest.app.rest.object.create;

import java.util.List;

import net.scholagest.app.rest.object.RestBaseRequest;

public class RestCreateBranchRequest extends RestBaseRequest {
    private String classKey;
    private List<String> keys;
    private List<String> values;

    public String getClassKey() {
        return classKey;
    }

    public List<String> getKeys() {
        return keys;
    }

    public List<String> getValues() {
        return values;
    }
}
