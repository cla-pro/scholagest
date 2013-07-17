package net.scholagest.objects;

import java.util.Map;

public class BaseObjectMock {
    public static BaseObject createBaseObject(String key, String type, Map<String, Object> properties) {
        BaseObject object = new BaseObject(key, type);
        object.setProperties(properties);
        return object;
    }

    public static BranchObject createBranchObject(String key, Map<String, Object> properties) {
        BranchObject object = new BranchObject(key);
        object.setProperties(properties);
        return object;
    }

    public static PeriodObject createPeriodObject(String key, Map<String, Object> properties) {
        PeriodObject object = new PeriodObject(key);
        object.setProperties(properties);
        return object;
    }

    public static ExamObject createExamObject(String key, Map<String, Object> properties) {
        ExamObject object = new ExamObject(key);
        object.setProperties(properties);
        return object;
    }
}
