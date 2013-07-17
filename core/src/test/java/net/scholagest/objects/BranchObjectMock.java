package net.scholagest.objects;

import java.util.Map;

public class BranchObjectMock {
    public static BranchObject createBranchObject(String key, Map<String, Object> properties) {
        BranchObject object = new BranchObject(key);
        object.setProperties(properties);
        return object;
    }
}
