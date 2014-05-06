package net.scholagest.old.objects;

import java.util.Map;

public interface ScholagestObject {
    public String getKey();

    public String getType();

    public Map<String, Object> getProperties();

    public Object getProperty(String propertyName);
}
