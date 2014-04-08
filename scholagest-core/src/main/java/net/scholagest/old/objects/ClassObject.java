package net.scholagest.old.objects;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;

public class ClassObject extends BaseObject {
    public ClassObject(String key) {
        super(key, CoreNamespace.tClass);
    }

    public ClassObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public DBSet getBranches() {
        return (DBSet) getProperty(CoreNamespace.pClassBranches);
    }

    public void setBranches(DBSet branches) {
        putProperty(CoreNamespace.pClassBranches, branches);
    }

    public DBSet getStudents() {
        return (DBSet) getProperty(CoreNamespace.pClassStudents);
    }

    public void setStudents(DBSet students) {
        putProperty(CoreNamespace.pClassStudents, students);
    }

    public DBSet getTeachers() {
        return (DBSet) getProperty(CoreNamespace.pClassTeachers);
    }

    public void setTeachers(DBSet teachers) {
        putProperty(CoreNamespace.pClassTeachers, teachers);
    }
    
    public String getYearKey() {
    	return (String) getProperty(CoreNamespace.pClassYear);
    }
    
    public void setYearKey(String yearKey) {
    	putProperty(CoreNamespace.pClassYear, yearKey);
    }
}
