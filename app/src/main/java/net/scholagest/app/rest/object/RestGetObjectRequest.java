package net.scholagest.app.rest.object;

import java.util.Set;

public class RestGetObjectRequest extends RestBaseRequest {
    private String key;
    private Set<String> properties;

    public String getKey() {
        return key;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public boolean arePropertiesEmpty() {
        return properties == null || properties.isEmpty();
    }
}
