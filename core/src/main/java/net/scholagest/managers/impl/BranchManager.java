package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class BranchManager extends ObjectManager implements IBranchManager {
    @Inject
    public BranchManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createBranch(String branchName, String className, String yearName) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String branchKey = CoreNamespace.branchNs + "/" + yearName + "/" + className + "#" + branchName;

        BaseObject branch = super.createObject(transaction, branchKey, CoreNamespace.tBranch);

        String periodSetKey = generateBranchPeriodsKey(branchKey);
        DBSet.createDBSet(transaction, periodSetKey);
        transaction.insert(branchKey, CoreNamespace.pBranchPeriods, periodSetKey, null);

        return branch;
    }

    private String generateBranchPeriodsKey(String branchKey) {
        return branchKey + "_periods";
    }

    @Override
    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        super.setObjectProperties(transaction, branchKey, branchProperties);
    }

    @Override
    public BaseObject getBranchProperties(String branchKey, Set<String> properties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BaseObject branch = new BaseObject(branchKey, CoreNamespace.tBranch);
        branch.setProperties(super.getObjectProperties(transaction, branchKey, properties));

        return branch;
    }
}
