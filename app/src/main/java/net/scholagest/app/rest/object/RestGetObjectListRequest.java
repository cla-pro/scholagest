package net.scholagest.app.rest.object;

import java.util.Set;

public class RestGetObjectListRequest extends RestBaseRequest {
    private Set<String> properties;

    public Set<String> getProperties() {
        return properties;
    }
}
