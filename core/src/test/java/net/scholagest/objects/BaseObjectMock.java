package net.scholagest.objects;

import java.util.Map;

public class BaseObjectMock {
    public static BaseObject createBaseObject(String key, String type, Map<String, Object> properties) {
        BaseObject object = new BaseObject(key, type);
        object.setProperties(properties);
        return object;
    }
}
