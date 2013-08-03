package net.scholagest.services.impl;

import java.util.Set;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IYearService;
import net.scholagest.services.kdom.DBToKdomConverter;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class YearService implements IYearService {
    private IDatabase database = null;
    private IYearBusinessComponent yearBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public YearService(IDatabase database, IYearBusinessComponent yearBusinessComponent, IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.yearBusinessComponent = yearBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public BaseObject startYear(String yearName) throws Exception {
        BaseObject year = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            BaseObject dbYear = yearBusinessComponent.startYear(yearName);
            year = new DBToKdomConverter().convertDbToKdom(dbYear, null);

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
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

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
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            BaseObject dbCurrentYear = yearBusinessComponent.getCurrentYearKey();
            currentYear = new DBToKdomConverter().convertDbToKdom(dbCurrentYear, null);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return currentYear;
    }

    @Override
    public Set<BaseObject> getYearsWithProperties(Set<String> propertyNames) throws Exception {
        Set<BaseObject> years = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            Set<BaseObject> dbYears = yearBusinessComponent.getYearsWithProperties(propertyNames);
            years = new DBToKdomConverter().convertDbSetToKdom(dbYears, propertyNames);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return years;
    }
}
