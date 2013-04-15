package net.scholagest.services.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IClassService;

import com.google.inject.Inject;

public class ClassService implements IClassService {
    private final IDatabase database;
    private final IClassBusinessComponent classBusinessComponent;

    @Inject
    public ClassService(IDatabase database, IClassBusinessComponent classBusinessComponent) {
        this.database = database;
        this.classBusinessComponent = classBusinessComponent;
    }

    @Override
    public BaseObject createClass(String requestId, Map<String, Object> classProperties) throws Exception {
        BaseObject clazz = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            clazz = classBusinessComponent.createClass(requestId, transaction, classProperties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return clazz;
    }

    @Override
    public Map<String, Set<BaseObject>> getClassesForYears(String requestId, Set<String> yearKeySet) throws Exception {
        Map<String, Set<BaseObject>> classes = new HashMap<>();

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            classes = classBusinessComponent.getClassesForYears(requestId, transaction, yearKeySet);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return classes;
    }

    @Override
    public BaseObject getClassProperties(String requestId, String classKey, Set<String> propertiesName) throws Exception {
        BaseObject classInfo = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            classInfo = classBusinessComponent.getClassProperties(requestId, transaction, classKey, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return classInfo;
    }

    @Override
    public void setClassProperties(String requestId, String classKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            classBusinessComponent.setClassProperties(requestId, transaction, classKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }
}
