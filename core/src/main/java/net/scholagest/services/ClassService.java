package net.scholagest.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;

import com.google.inject.Inject;

public class ClassService implements IClassService {
    private IDatabase database = null;
    private IClassBusinessComponent classBusinessComponent = null;

    @Inject
    public ClassService(IDatabase database, IClassBusinessComponent classBusinessComponent) {
        this.database = database;
        this.classBusinessComponent = classBusinessComponent;
    }

    @Override
    public String createClass(String requestId, Map<String, Object> classProperties) throws Exception {
        String classKey = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            classKey = classBusinessComponent.createClass(requestId, transaction, classProperties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return classKey;
    }

    @Override
    public Map<String, Set<String>> getClasses(String requestId, Set<String> yearKeySet) throws Exception {
        Map<String, Set<String>> classesKey = new HashMap<>();

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            classesKey = classBusinessComponent.getClassesForYear(requestId, transaction, yearKeySet);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return classesKey;
    }

    @Override
    public Map<String, Object> getClassProperties(String requestId, String classKey, Set<String> propertiesName) throws Exception {
        Map<String, Object> classInfo = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            classInfo = classBusinessComponent.getClassProperties(requestId, transaction, classKey, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return classInfo;
    }

    // @Override
    // public void removeClass(String classKey) throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void assignTeacherToClass(String classKey, String teacherKey)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void unassignTeacherToClass(String classKey, String teacherKey)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void assignStudentToClass(String classKey, String studentKey)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void unassignStudentToClass(String classKey, String studentKey)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void addBranch(String classKey, String branchKey) throws Exception
    // {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // @Override
    // public void setClassInfo(String classKey, Map<String, Object> properties)
    // throws Exception {
    // // TODO Auto-generated method stub
    //
    // }
}
