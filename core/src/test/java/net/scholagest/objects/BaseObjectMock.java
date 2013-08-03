package net.scholagest.objects;

import java.util.HashMap;
import java.util.Map;

public class BaseObjectMock {
    public static BaseObject createBaseObject(String key, String type, Map<String, Object> properties) {
        BaseObject object = new BaseObject(key, type);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static BranchObject createBranchObject(String key, Map<String, Object> properties) {
        BranchObject object = new BranchObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static PeriodObject createPeriodObject(String key, Map<String, Object> properties) {
        PeriodObject object = new PeriodObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static ExamObject createExamObject(String key, Map<String, Object> properties) {
        ExamObject object = new ExamObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static ClassObject createClassObject(String key, Map<String, Object> properties) {
        ClassObject object = new ClassObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static StudentObject createStudentObject(String key, Map<String, Object> properties) {
        StudentObject object = new StudentObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static TeacherObject createTeacherObject(String key, Map<String, Object> properties) {
        TeacherObject object = new TeacherObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }

    public static UserObject createUserObject(String key, HashMap<String, Object> properties) {
        UserObject object = new UserObject(key);
        object.setProperties(properties);
        object.flushAllProperties();
        return object;
    }
}
