package net.scholagest.services.impl;

import java.util.Set;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IYearService;

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
    public BaseObject startYear(String requestId, String yearName) throws Exception {
        BaseObject year = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            year = yearBusinessComponent.startYear(requestId, transaction, yearName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return year;
    }

    @Override
    public void stopYear(String requestId) throws Exception {
        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            yearBusinessComponent.stopYear(requestId, transaction);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public BaseObject getCurrentYearKey(String requestId) throws Exception {
        BaseObject currentYear = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            currentYear = yearBusinessComponent.getCurrentYearKey(requestId, transaction);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return currentYear;
    }

    // @Override
    // public Set<String> getYearClasses(String yearKey) throws Exception {
    // // TODO Auto-generated method stub
    // return null;
    // }
    //
    // @Override
    // public void addClass(String yearKey, String classKey) throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void setInfo(String yearKey, Map<String, Object> properties)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public Map<String, Object> getInfo(String yearKey, Set<String>
    // propertiesName) throws Exception {
    // // TODO Auto-generated method stub
    // return null;
    // }

    @Override
    public Set<BaseObject> getYearsWithProperties(String requestId, Set<String> properties) throws Exception {
        Set<BaseObject> years = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            years = yearBusinessComponent.getYearsWithProperties(requestId, transaction, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return years;
    }
}
