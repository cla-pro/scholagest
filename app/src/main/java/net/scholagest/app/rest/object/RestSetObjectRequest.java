package net.scholagest.app.rest.object;

import java.util.Map;

public class RestSetObjectRequest extends RestBaseRequest {
    private String key;
    private Map<String, Object> properties;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
