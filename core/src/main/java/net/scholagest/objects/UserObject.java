package net.scholagest.objects;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.impl.AuthorizationNamespace;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;

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
