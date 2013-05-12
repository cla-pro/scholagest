package net.scholagest.objects;

import java.util.HashMap;
import java.util.Map;

public class BaseObject implements ScholagestObject {
    private String key;
    private String type;
    private Map<String, Object> properties;

    public BaseObject(String key, String type) {
        this.key = key;
        this.type = type;
        this.properties = new HashMap<>();
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        if (properties == null) {
            this.properties = new HashMap<>();
        } else {
            this.properties = properties;
        }
    }

    @Override
    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public void putProperty(String propertyName, Object value) {
        properties.put(propertyName, value);
    }
}
