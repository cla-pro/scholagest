package net.scholagest.services.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IBranchService;

import com.google.inject.Inject;

public class BranchService implements IBranchService {
    private final IDatabase database;
    private final IBranchBusinessComponent branchBusinessComponent;

    @Inject
    public BranchService(IDatabase database, IBranchBusinessComponent branchBusinessComponent) {
        this.database = database;
        this.branchBusinessComponent = branchBusinessComponent;
    }

    @Override
    public BaseObject createBranch(String requestId, String classKey, Map<String, Object> branchProperties) throws Exception {
        BaseObject branch = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            branch = branchBusinessComponent.createBranch(requestId, transaction, classKey, branchProperties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return branch;
    }

    @Override
    public BaseObject getBranchProperties(String requestId, String branchKey, Set<String> propertiesName) throws Exception {
        BaseObject branchInfo = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            branchInfo = branchBusinessComponent.getBranchProperties(requestId, transaction, branchKey, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return branchInfo;
    }

    @Override
    public void setBranchProperties(String requestId, String branchKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            branchBusinessComponent.setBranchProperties(requestId, transaction, branchKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
