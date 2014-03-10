package net.scholagest.old.services.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.business.IExamBusinessComponent;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.namespace.AuthorizationRolesNamespace;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.services.IExamService;
import net.scholagest.old.services.kdom.DBToKdomConverter;
import net.scholagest.old.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
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

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            BaseObject dbExam = examBusinessComponent.createExam(yearKey, classKey, branchKey, periodKey, examInfo);
            exam = new DBToKdomConverter().convertDbToKdom(dbExam, null);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return exam;
    }

    @Override
    public BaseObject getExamProperties(String examKey, Set<String> propertiesName) throws Exception {
        BaseObject exam = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(examKey);
            if (classKey == null) {
                return null;
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            BaseObject dbExam = examBusinessComponent.getExamProperties(examKey, propertiesName);
            exam = new DBToKdomConverter().convertDbToKdom(dbExam, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return exam;
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
