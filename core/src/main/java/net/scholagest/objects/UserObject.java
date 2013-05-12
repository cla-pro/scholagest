package net.scholagest.objects;

import net.scholagest.managers.impl.AuthorizationNamespace;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.types.DBSet;

public class UserObject extends BaseObject {
    public UserObject(String key) {
        super(key, CoreNamespace.tUser);
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
