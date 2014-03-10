package net.scholagest.old.managers.impl;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.IPeriodManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.ObjectHelper;
import net.scholagest.old.objects.PeriodObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class PeriodManager extends ObjectManager implements IPeriodManager {
    @Inject
    protected PeriodManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public PeriodObject createPeriod(String periodName, String classKey) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String periodKey = generatePeriodKey();

        String examSetKey = generatePeriodExamsKey(periodKey);
        DBSet examSet = DBSet.createDBSet(transaction, examSetKey);

        PeriodObject period = new PeriodObject(periodKey);
        period.setExams(examSet);
        period.setClassKey(classKey);
        period.putProperty(CoreNamespace.pPeriodName, periodName);

        persistObject(transaction, period);

        return period;
    }

    private String generatePeriodKey() {
        return CoreNamespace.periodNs + "/" + UUID.randomUUID().toString();
    }

    private String generatePeriodExamsKey(String periodKey) {
        return periodKey + "_exams";
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        PeriodObject period = new PeriodObject(transaction, new ObjectHelper(getOntologyManager()), periodKey);
        period.putAllProperties(periodProperties);
    }

    @Override
    public PeriodObject getPeriodProperties(String periodKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new PeriodObject(transaction, new ObjectHelper(getOntologyManager()), periodKey);
    }
}
