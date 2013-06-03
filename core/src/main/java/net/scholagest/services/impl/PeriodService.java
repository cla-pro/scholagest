package net.scholagest.services.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IPeriodBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IPeriodService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class PeriodService implements IPeriodService {
    private final IDatabase database;
    private final IPeriodBusinessComponent periodBusinessComponent;

    @Inject
    public PeriodService(IDatabase database, IPeriodBusinessComponent periodBusinessComponent) {
        this.database = database;
        this.periodBusinessComponent = periodBusinessComponent;
    }

    @Override
    public void setPeriodProperties(String periodKey, Map<String, Object> periodProperties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(periodKey);
            if (classKey == null) {
                return;
            }

            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(classKey));

            periodBusinessComponent.setPeriodProperties(periodKey, periodProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getPeriodProperties(String periodKey, Set<String> properties) throws Exception {
        BaseObject periodInfo = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            String classKey = getClassKey(periodKey);
            if (classKey == null) {
                return null;
            }

            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(classKey));

            periodInfo = periodBusinessComponent.getPeriodProperties(periodKey, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return periodInfo;
    }

    private String getClassKey(String periodKey) throws Exception {
        Set<String> examClassProperties = new HashSet<>(Arrays.asList(CoreNamespace.pPeriodClass));
        BaseObject prop = periodBusinessComponent.getPeriodProperties(periodKey, examClassProperties);
        if (prop != null) {
            return (String) prop.getProperty(CoreNamespace.pPeriodClass);
        }

        return null;
    }
}
