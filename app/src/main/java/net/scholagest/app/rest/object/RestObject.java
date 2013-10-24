package net.scholagest.app.rest.object;

import java.util.Map;

public class RestObject {
    private String key;
    private String type;
    private Map<String, RestProperty> properties;
    private boolean writable = false;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, RestProperty> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, RestProperty> properties) {
        this.properties = properties;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    @Override
    public String toString() {
        return String.format("RestObject [key: %s, type: %s, properties: %s]", key, type, properties.toString());
    }
}
