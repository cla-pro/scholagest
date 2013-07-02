package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class PeriodManager extends ObjectManager implements IPeriodManager {
    @Inject
    protected PeriodManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createPeriod(String periodName, String branchName, String className, String yearName) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String periodKey = CoreNamespace.periodNs + "/" + yearName + "/" + className + "/" + branchName + "#" + periodName;

        BaseObject period = super.createObject(transaction, periodKey, CoreNamespace.tPeriod);

        String examSetKey = generatePeriodExamsKey(periodKey);
        DBSet.createDBSet(transaction, examSetKey);
        transaction.insert(periodKey, CoreNamespace.pPeriodExams, examSetKey, null);

        return period;
    }

    private String generatePeriodExamsKey(String periodKey) {
        return periodKey + "_exams";
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        super.setObjectProperties(transaction, periodKey, periodProperties);
    }

    @Override
    public BaseObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BaseObject branch = new BaseObject(periodKey, CoreNamespace.tPeriod);
        branch.setProperties(super.getObjectProperties(transaction, periodKey, properties));

        return branch;
    }
}
