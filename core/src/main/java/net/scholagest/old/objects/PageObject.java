package net.scholagest.old.objects;

import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;

public class PageObject extends BaseObject {
    public PageObject(String key) {
        super(key, CoreNamespace.tPage);
    }

    public PageObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public void setPath(String path) {
        putProperty(CoreNamespace.pPagePath, path);
    }

    public String getPath() {
        return (String) getProperty(CoreNamespace.pPagePath);
    }

    public void setRoles(DBSet roles) {
        putProperty(CoreNamespace.pPageRoles, roles);
    }

    public void addRole(String role) throws DatabaseException {
        DBSet roleSet = getRoles();
        roleSet.add(role);
    }

    public DBSet getRoles() {
        return (DBSet) getProperty(CoreNamespace.pPageRoles);
    }
}
