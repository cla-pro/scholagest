package net.scholagest.objects;

import net.scholagest.database.DatabaseException;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.types.DBSet;

public class PageObject extends BaseObject {
    public PageObject(String key) {
        super(key, CoreNamespace.tPage);
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
