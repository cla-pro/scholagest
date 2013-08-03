package net.scholagest.objects;

import net.scholagest.database.ITransaction;
import net.scholagest.namespace.CoreNamespace;

public class TeacherObject extends BaseObject {
    public TeacherObject(String key) {
        super(key, CoreNamespace.tTeacher);
    }

    public TeacherObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public String getUserKey() {
        return (String) getProperty(CoreNamespace.pTeacherUser);
    }

    public void setUserKey(String userKey) {
        putProperty(CoreNamespace.pTeacherUser, userKey);
    }
}
