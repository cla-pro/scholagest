package net.scholagest.old.objects;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.namespace.CoreNamespace;

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
