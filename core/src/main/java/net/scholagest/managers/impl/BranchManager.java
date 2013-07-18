package net.scholagest.managers.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BranchObject;
import net.scholagest.objects.BranchType;
import net.scholagest.objects.ObjectHelper;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class BranchManager extends ObjectManager implements IBranchManager {
    @Inject
    public BranchManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BranchObject createBranch(String branchName, String classKey, String className, String yearName, Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String branchKey = CoreNamespace.branchNs + "/" + yearName + "/" + className + "#" + branchName;
        BranchObject branch = new BranchObject(branchKey);

        branch.putAllProperties(properties);

        String periodSetKey = generateBranchPeriodsKey(branchKey);
        DBSet periodSet = DBSet.createDBSet(transaction, periodSetKey);

        branch.setPeriods(periodSet);
        branch.setClassKey(classKey);

        if (branch.getBranchType() == null) {
            branch.setBranchType(BranchType.NUMERICAL);
        }

        persistObject(transaction, branch);

        return branch;
    }

    private String generateBranchPeriodsKey(String branchKey) {
        return branchKey + "_periods";
    }

    @Override
    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BranchObject branch = new BranchObject(transaction, new ObjectHelper(getOntologyManager()), branchKey);
        branch.putAllProperties(branchProperties);
    }

    @Override
    public BranchObject getBranchProperties(String branchKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new BranchObject(transaction, new ObjectHelper(getOntologyManager()), branchKey);
    }
}
