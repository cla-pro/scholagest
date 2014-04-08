package net.scholagest.old.objects;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.namespace.CoreNamespace;

public class StudentObject extends BaseObject {
    public StudentObject(String key) {
        super(key, CoreNamespace.tStudent);
    }

    public StudentObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public String getPersonalInfoKey() {
        return (String) getProperty(CoreNamespace.pStudentPersonalInfo);
    }

    public void setPersonalInfoKey(String personalInfoKey) {
        putProperty(CoreNamespace.pStudentPersonalInfo, personalInfoKey);
    }

    public String getMedicalInfoKey() {
        return (String) getProperty(CoreNamespace.pStudentMedicalInfo);
    }

    public void setMedicalInfoKey(String medicalInfoKey) {
        putProperty(CoreNamespace.pStudentMedicalInfo, medicalInfoKey);
    }
}
