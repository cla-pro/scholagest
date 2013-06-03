package net.scholagest.services.impl;

import java.util.Set;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IYearService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class YearService implements IYearService {
    private IDatabase database = null;
    private IYearBusinessComponent yearBusinessComponent;

    @Inject
    public YearService(IDatabase database, IYearBusinessComponent yearBusinessComponent) {
        this.database = database;
        this.yearBusinessComponent = yearBusinessComponent;
    }

    @Override
    public BaseObject startYear(String yearName) throws Exception {
        BaseObject year = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAdminRole());

            year = yearBusinessComponent.startYear(yearName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return year;
    }

    @Override
    public void stopYear() throws Exception {
        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAdminRole());

            yearBusinessComponent.stopYear();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getCurrentYearKey() throws Exception {
        BaseObject currentYear = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            currentYear = yearBusinessComponent.getCurrentYearKey();

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return currentYear;
    }

    @Override
    public Set<BaseObject> getYearsWithProperties(Set<String> properties) throws Exception {
        Set<BaseObject> years = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            years = yearBusinessComponent.getYearsWithProperties(properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return years;
    }
}
