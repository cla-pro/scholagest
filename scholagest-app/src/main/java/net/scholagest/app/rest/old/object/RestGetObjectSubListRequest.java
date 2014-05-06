package net.scholagest.app.rest.old.object;

import java.util.Set;

public class RestGetObjectSubListRequest extends RestBaseRequest {
    private Set<String> keys;
    private Set<String> properties;

    public Set<String> getKeys() {
        return keys;
    }

    public Set<String> getProperties() {
        return properties;
    }

}
