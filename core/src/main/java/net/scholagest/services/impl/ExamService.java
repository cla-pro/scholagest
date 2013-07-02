package net.scholagest.services.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IExamBusinessComponent;
import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IExamService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ExamService implements IExamService {
    private final IDatabase database;
    private final IExamBusinessComponent examBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public ExamService(IDatabase database, IExamBusinessComponent examBusinessComponent, IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.examBusinessComponent = examBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public BaseObject createExam(String yearKey, String classKey, String branchKey, String periodKey, Map<String, Object> examInfo) throws Exception {
        BaseObject exam = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            exam = examBusinessComponent.createExam(yearKey, classKey, branchKey, periodKey, examInfo);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return exam;
    }

    @Override
    public BaseObject getExamProperties(String examKey, Set<String> propertiesName) throws Exception {
        BaseObject properties = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(examKey);
            if (classKey == null) {
                return null;
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            properties = examBusinessComponent.getExamProperties(examKey, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return properties;
    }

    private String getClassKey(String examKey) throws Exception {
        Set<String> examClassProperties = new HashSet<>(Arrays.asList(CoreNamespace.pExamClass));
        BaseObject prop = examBusinessComponent.getExamProperties(examKey, examClassProperties);
        if (prop != null) {
            return (String) prop.getProperty(CoreNamespace.pExamClass);
        }

        return null;
    }
}
