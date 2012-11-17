package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IYearBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;

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
    public String startYear(String requestId, String yearName) throws Exception {
        String yearKey = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            yearKey = yearBusinessComponent.startYear(requestId, transaction, yearName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return yearKey;
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
    public String getCurrentYearKey(String requestId) throws Exception {
        String currentYearKey = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            currentYearKey = yearBusinessComponent.getCurrentYearKey(requestId, transaction);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return currentYearKey;
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
    public Map<String, Map<String, Object>> getYearsWithProperties(String requestId, Set<String> properties) throws Exception {
        Map<String, Map<String, Object>> years = null;

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
