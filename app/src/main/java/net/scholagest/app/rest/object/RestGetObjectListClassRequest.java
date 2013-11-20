package net.scholagest.app.rest.object;

import java.util.Set;

public class RestGetObjectListClassRequest extends RestGetObjectListRequest {
    private Set<String> yearKeys;

    public Set<String> getYearKeys() {
        return yearKeys;
    }
}
