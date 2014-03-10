package net.scholagest.old.managers;

import java.util.Map;
import java.util.Set;

public interface IObjectManager {
    public String createObject(String objectKey, String type) throws Exception;

    public Map<String, Object> getObjectProperties(String objectKey, Set<String> propertiesName) throws Exception;

    public void setObjectProperties(String objectKey, Map<String, Object> properties) throws Exception;
}
