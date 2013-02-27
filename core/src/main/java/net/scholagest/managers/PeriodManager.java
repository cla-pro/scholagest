package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class PeriodManager extends ObjectManager implements IPeriodManager {
    @Inject
    protected PeriodManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createPeriod(String requestId, ITransaction transaction, String periodName, String branchName, String className, String yearName)
            throws Exception {
        String periodKey = CoreNamespace.periodNs + "/" + yearName + "/" + className + "/" + branchName + "#" + periodName;

        BaseObject period = super.createObject(requestId, transaction, periodKey, CoreNamespace.tPeriod);

        String examSetKey = generatePeriodExamsKey(periodKey);
        DBSet.createDBSet(transaction, examSetKey);
        transaction.insert(periodKey, CoreNamespace.pPeriodExams, examSetKey, null);

        return period;
    }

    private String generatePeriodExamsKey(String periodKey) {
        return periodKey + "_exams";
    }

    @Override
    public void setPeriodProperties(String requestId, ITransaction transaction, String periodKey, Map<String, Object> periodProperties)
            throws Exception {
        super.setObjectProperties(requestId, transaction, periodKey, periodProperties);
    }

    @Override
    public BaseObject getPeriodProperties(String requestId, ITransaction transaction, String periodKey, Set<String> properties) throws Exception {
        BaseObject branch = new BaseObject(periodKey, CoreNamespace.tPeriod);
        branch.setProperties(super.getObjectProperties(requestId, transaction, periodKey, properties));

        return branch;
    }
}
