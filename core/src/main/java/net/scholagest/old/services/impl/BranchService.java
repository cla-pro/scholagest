package net.scholagest.old.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.business.IBranchBusinessComponent;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.namespace.AuthorizationRolesNamespace;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.BranchObject;
import net.scholagest.old.services.IBranchService;
import net.scholagest.old.services.kdom.DBToKdomConverter;
import net.scholagest.old.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class BranchService implements IBranchService {
    private final IDatabase database;
    private final IBranchBusinessComponent branchBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public BranchService(IDatabase database, IBranchBusinessComponent branchBusinessComponent, IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.branchBusinessComponent = branchBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public BaseObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception {
        BaseObject branch = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            BranchObject dbBranch = branchBusinessComponent.createBranch(classKey, branchProperties);
            branch = new DBToKdomConverter().convertDbToKdom(dbBranch, null);

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

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(branchKey);
            if (classKey == null) {
                return null;
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            BranchObject dbBranch = branchBusinessComponent.getBranchProperties(branchKey, propertiesName);
            branchInfo = new DBToKdomConverter().convertDbToKdom(dbBranch, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return branchInfo;
    }

    @Override
    public void setBranchProperties(String branchKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(branchKey);
            if (classKey == null) {
                return;
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            branchBusinessComponent.setBranchProperties(branchKey, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public Map<String, Map<String, BaseObject>> getBranchMeans(String branchKey, Set<String> studentKeys) throws Exception {
        Map<String, Map<String, BaseObject>> means = new HashMap<>();

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(branchKey);
            if (classKey == null) {
                return new HashMap<>();
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            Map<String, Map<String, BaseObject>> dbMeans = branchBusinessComponent.getPeriodMeans(branchKey, studentKeys);
            for (String examKey : dbMeans.keySet()) {
                Map<String, BaseObject> studentMeans = new HashMap<>();
                Map<String, BaseObject> dbStudentMeans = dbMeans.get(examKey);
                for (String studentKey : dbStudentMeans.keySet()) {
                    BaseObject converted = new DBToKdomConverter().convertDbToKdom(dbStudentMeans.get(studentKey), null);
                    studentMeans.put(studentKey, converted);
                }

                means.put(examKey, studentMeans);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return means;
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
