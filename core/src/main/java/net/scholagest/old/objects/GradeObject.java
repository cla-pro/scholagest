package net.scholagest.old.objects;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.namespace.CoreNamespace;

public class GradeObject extends BaseObject {
    public GradeObject(String key) {
        super(key, CoreNamespace.tGrade);
    }

    public GradeObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public Double getGrade() {
        String stringGrade = (String) getProperty(CoreNamespace.pGradeValue);
        if (stringGrade == null) {
            return null;
        }
        return Double.parseDouble(stringGrade);
    }

    public void setGrade(Double grade) {
        putProperty(CoreNamespace.pGradeValue, grade.toString());
    }
}
