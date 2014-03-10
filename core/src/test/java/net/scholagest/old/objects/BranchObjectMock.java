package net.scholagest.old.objects;

import java.util.Map;

import net.scholagest.old.objects.BranchObject;

public class BranchObjectMock {
    public static BranchObject createBranchObject(String key, Map<String, Object> properties) {
        BranchObject object = new BranchObject(key);
        object.setProperties(properties);
        return object;
    }
}
