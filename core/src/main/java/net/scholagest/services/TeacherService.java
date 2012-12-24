package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

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
    public BaseObject createTeacher(String requestId, String teacherType, Map<String, Object> teacherProperties) throws Exception {
        BaseObject teacher = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            teacher = teacherBusinessComponent.createTeacher(requestId, transaction, teacherType, teacherProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return teacher;
    }

    @Override
    public Set<BaseObject> getTeachers(String requestId) throws Exception {
        Set<BaseObject> teachers = null;

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
    public Set<BaseObject> getTeachersWithProperties(String requestId, Set<String> propertiesName) throws Exception {
        Set<BaseObject> teachers = null;

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
    public BaseObject getTeacherProperties(String requestId, String teacherKey, Set<String> propertiesName) throws Exception {
        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            BaseObject properties = teacherBusinessComponent.getTeacherProperties(requestId, transaction, teacherKey, propertiesName);
            transaction.commit();
            return properties;
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return null;
    }
}
