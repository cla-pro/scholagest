package net.scholagest.services;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;

import com.google.inject.Inject;

public class TeacherService implements ITeacherService {
    private IDatabase database = null;
    private ITeacherBusinessComponent teacherBusinessComponent = null;

    @Inject
    public TeacherService(IDatabase database, ITeacherBusinessComponent teacherBusinessComponent) {
        this.database = database;
        this.teacherBusinessComponent = teacherBusinessComponent;
    }

    // @Override
    // public Set<String> getTeacherTypes() throws Exception {
    // // TODO Auto-generated method stub
    // return null;
    // }

    @Override
    public String createTeacher(String requestId, String teacherType, Map<String, Object> teacherProperties) throws Exception {
        String teacherKey = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            teacherKey = teacherBusinessComponent.createTeacher(requestId, transaction, teacherType, teacherProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return teacherKey;
    }

    @Override
    public Set<String> getTeachers(String requestId) throws Exception {
        Set<String> teachers = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            teachers = teacherBusinessComponent.getTeachers(requestId, transaction);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return teachers;
    }

    @Override
    public Map<String, Map<String, Object>> getTeachersWithProperties(String requestId, Set<String> propertiesName) throws Exception {
        Map<String, Map<String, Object>> teachers = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            teachers = teacherBusinessComponent.getTeachersWithProperties(requestId, transaction, propertiesName);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return teachers;
    }

    // @Override
    // public Set<String> getTeacherClasses(String requestId, String teacherKey)
    // throws Exception {
    // // TODO Auto-generated method stub
    // return null;
    // }

    @Override
    public void setTeacherProperties(String requestId, String teacherKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            teacherBusinessComponent.setTeacherProperties(requestId, transaction, teacherKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Map<String, Object> getTeacherProperties(String requestId, String teacherKey, Set<String> propertiesName) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            Map<String, Object> properties = teacherBusinessComponent.getTeacherProperties(requestId, transaction, teacherKey, propertiesName);
            transaction.commit();
            return properties;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return new HashMap<>();
    }
}
