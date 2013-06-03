package net.scholagest.services.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IBranchBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IBranchService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;

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
    public BaseObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception {
        BaseObject branch = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        try {
            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(classKey));

            branch = branchBusinessComponent.createBranch(classKey, branchProperties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return branch;
    }

    @Override
    public BaseObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception {
        BaseObject branchInfo = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        try {
            String classKey = getClassKey(branchKey);
            if (classKey == null) {
                return null;
            }

            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(classKey));

            branchInfo = branchBusinessComponent.getBranchProperties(branchKey, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return branchInfo;
    }

    @Override
    public void setBranchProperties(String branchKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        try {
            String classKey = getClassKey(branchKey);
            if (classKey == null) {
                return;
            }

            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(classKey));

            branchBusinessComponent.setBranchProperties(branchKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private String getClassKey(String branchKey) throws Exception {
        Set<String> examClassProperties = new HashSet<>(Arrays.asList(CoreNamespace.pBranchClass));
        BaseObject prop = branchBusinessComponent.getBranchProperties(branchKey, examClassProperties);
        if (prop != null) {
            return (String) prop.getProperty(CoreNamespace.pBranchClass);
        }

        return null;
    }
}
