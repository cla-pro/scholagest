package net.scholagest.objects;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;

public class ExamObject extends BaseObject {
    private static final int DEFAULT_COEFF = 1;

    public ExamObject(String key) {
        super(key, CoreNamespace.tExam);
    }

    public ExamObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public String getClassKey() {
        return (String) getProperty(CoreNamespace.pExamClass);
    }

    public void setClassKey(String classKey) {
        putProperty(CoreNamespace.pExamClass, classKey);
    }

    public int getCoeff() {
        String coeff = (String) getProperty(CoreNamespace.pExamCoeff);

        if (coeff == null) {
            setCoeff(DEFAULT_COEFF);
            return DEFAULT_COEFF;
        } else {
            return Integer.parseInt(coeff);
        }
    }

    public void setCoeff(int coeff) {
        putProperty(CoreNamespace.pExamCoeff, "" + coeff);
    }

    public DBSet getGrades() {
        return (DBSet) getProperty(CoreNamespace.pExamGrades);
    }

    public void setGrades(DBSet grades) {
        putProperty(CoreNamespace.pExamGrades, grades);
    }
}
