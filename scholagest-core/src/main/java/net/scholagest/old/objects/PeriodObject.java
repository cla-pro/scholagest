package net.scholagest.old.objects;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;

public class PeriodObject extends BaseObject {
    public PeriodObject(String key) {
        super(key, CoreNamespace.tPeriod);
    }

    public PeriodObject(ITransaction transaction, ObjectHelper objectHelper, String key) {
        super(transaction, objectHelper, key);
    }

    public String getClassKey() {
        return (String) getProperty(CoreNamespace.pPeriodClass);
    }

    public void setClassKey(String classKey) {
        putProperty(CoreNamespace.pPeriodClass, classKey);
    }

    public DBSet getExams() {
        return (DBSet) getProperty(CoreNamespace.pPeriodExams);
    }

    public void setExams(DBSet exams) {
        putProperty(CoreNamespace.pPeriodExams, exams);
    }

    public String getMeanKey() {
        return (String) getProperty(CoreNamespace.pPeriodMean);
    }

    public void setMeanKey(String meanKey) {
        putProperty(CoreNamespace.pPeriodMean, meanKey);
    }
}
