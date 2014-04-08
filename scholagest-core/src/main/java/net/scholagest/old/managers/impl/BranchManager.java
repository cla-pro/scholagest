package net.scholagest.old.managers.impl;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IBranchManager;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BranchObject;
import net.scholagest.old.objects.BranchType;
import net.scholagest.old.objects.ObjectHelper;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class BranchManager extends ObjectManager implements IBranchManager {
    @Inject
    public BranchManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public boolean checkWhetherBranchExistsInClass(String branchName, String className, String yearName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String branchesBasePropertyName = generateBranchesBasePropertyName(branchName, className, yearName);
        String branchKey = (String) transaction.get(CoreNamespace.branchesBase, branchesBasePropertyName, null);

        return branchKey != null;
    }

    @Override
    public BranchObject createBranch(String branchName, String classKey, String className, String yearName, Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String branchKey = CoreNamespace.branchNs + "#" + UUID.randomUUID().toString();
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

        String branchesBasePropertyName = generateBranchesBasePropertyName(branchName, className, yearName);
        transaction.insert(CoreNamespace.branchesBase, branchesBasePropertyName, branchKey, null);

        return branch;
    }

    private String generateBranchesBasePropertyName(String branchName, String className, String yearName) {
        return yearName + "/" + className + "/" + branchName;
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
