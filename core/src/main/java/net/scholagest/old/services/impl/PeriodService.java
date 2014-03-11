package net.scholagest.old.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.business.IPeriodBusinessComponent;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.namespace.AuthorizationRolesNamespace;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.PeriodObject;
import net.scholagest.old.services.IPeriodService;
import net.scholagest.old.services.kdom.DBToKdomConverter;
import net.scholagest.old.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class PeriodService implements IPeriodService {
    private final IDatabase database;
    private final IPeriodBusinessComponent periodBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public PeriodService(IDatabase database, IPeriodBusinessComponent periodBusinessComponent, IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.periodBusinessComponent = periodBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(periodKey);
            if (classKey == null) {
                return;
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            periodBusinessComponent.setPeriodProperties(periodKey, periodProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getPeriodProperties(String periodKey, Set<String> propertyNames) throws Exception {
        BaseObject period = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(periodKey);
            if (classKey == null) {
                return null;
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            BaseObject dbPeriod = periodBusinessComponent.getPeriodProperties(periodKey, propertyNames);
            period = new DBToKdomConverter().convertDbToKdom(dbPeriod, propertyNames);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return period;
    }

    @Override
    public Map<String, Map<String, BaseObject>> getPeriodMeans(String periodKey, Set<String> studentKeys) throws Exception {
        Map<String, Map<String, BaseObject>> means = new HashMap<>();

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(periodKey);
            if (classKey == null) {
                return new HashMap<>();
            }

            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(classKey));

            Map<String, Map<String, BaseObject>> dbMeans = periodBusinessComponent.getPeriodMeans(periodKey, studentKeys);
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

    private String getClassKey(String periodKey) throws Exception {
        Set<String> examClassProperties = new HashSet<>(Arrays.asList(CoreNamespace.pPeriodClass));
        PeriodObject periodObject = periodBusinessComponent.getPeriodProperties(periodKey, examClassProperties);
        if (periodObject != null) {
            return periodObject.getClassKey();
        }

        return null;
    }
}
