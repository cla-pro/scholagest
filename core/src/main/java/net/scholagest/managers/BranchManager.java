package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class BranchManager extends ObjectManager implements IBranchManager {
    @Inject
    public BranchManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createBranch(String requestId, ITransaction transaction, String branchName, String className, String yearName) throws Exception {
        String branchKey = CoreNamespace.branchNs + "/" + yearName + "/" + className + "#" + branchName;

        BaseObject branch = super.createObject(requestId, transaction, branchKey, CoreNamespace.tBranch);

        String periodSetKey = generateBranchPeriodsKey(branchKey);
        DBSet.createDBSet(transaction, periodSetKey);
        transaction.insert(branchKey, CoreNamespace.pBranchPeriods, periodSetKey, null);

        return branch;
    }

    private String generateBranchPeriodsKey(String branchKey) {
        return branchKey + "_periods";
    }

    @Override
    public void setBranchProperties(String requestId, ITransaction transaction, String branchKey, Map<String, Object> branchProperties)
            throws Exception {
        super.setObjectProperties(requestId, transaction, branchKey, branchProperties);
    }

    @Override
    public BaseObject getBranchProperties(String requestId, ITransaction transaction, String branchKey, Set<String> properties) throws Exception {
        BaseObject branch = new BaseObject(branchKey, CoreNamespace.tBranch);
        branch.setProperties(super.getObjectProperties(requestId, transaction, branchKey, properties));

        return branch;
    }
}
