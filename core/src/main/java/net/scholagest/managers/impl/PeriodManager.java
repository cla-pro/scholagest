package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.PeriodObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class PeriodManager extends ObjectManager implements IPeriodManager {
    @Inject
    protected PeriodManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public PeriodObject createPeriod(String periodName, String classKey, String branchName, String className, String yearName) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String periodKey = CoreNamespace.periodNs + "/" + yearName + "/" + className + "/" + branchName + "#" + periodName;

        String examSetKey = generatePeriodExamsKey(periodKey);
        DBSet examSet = DBSet.createDBSet(transaction, examSetKey);

        PeriodObject period = new PeriodObject(periodKey);
        period.setExams(examSet);
        period.setClassKey(classKey);
        period.putProperty(CoreNamespace.pPeriodName, periodName);

        persistObject(transaction, period);

        return period;
    }

    private String generatePeriodExamsKey(String periodKey) {
        return periodKey + "_exams";
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        setObjectProperties(transaction, periodKey, periodProperties);
    }

    @Override
    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        PeriodObject branch = new PeriodObject(periodKey);
        branch.setProperties(getObjectProperties(transaction, periodKey, properties));

        return branch;
    }
}
