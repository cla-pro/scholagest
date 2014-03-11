package net.scholagest.old.objects;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.impl.AuthorizationNamespace;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;

public class UserObject extends BaseObject {
    public UserObject(String key) {
        super(key, CoreNamespace.tUser);
    }

    public UserObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public String getUsername() {
        return (String) getProperty(CoreNamespace.pUserUsername);
    }

    public void setUsername(String username) {
        putProperty(CoreNamespace.pUserUsername, username);
    }

    public String getPassword() {
        return (String) getProperty(CoreNamespace.pUserPassword);
    }

    public void setPassword(String password) {
        putProperty(CoreNamespace.pUserPassword, password);
    }

    public String getTeacherKey() {
        return (String) getProperty(CoreNamespace.pUserTeacher);
    }

    public void setTeacherKey(String teacherKey) {
        putProperty(CoreNamespace.pUserTeacher, teacherKey);
    }

    public DBSet getRoles() {
        return (DBSet) getProperty(AuthorizationNamespace.pUserRoles);
    }

    public void setRoles(DBSet roles) {
        putProperty(AuthorizationNamespace.pUserRoles, roles);
    }

    public DBSet getPermissions() {
        return (DBSet) getProperty(AuthorizationNamespace.pUserPermissions);
    }

    public void setPermissions(DBSet permissions) {
        putProperty(AuthorizationNamespace.pUserPermissions, permissions);
    }
}
