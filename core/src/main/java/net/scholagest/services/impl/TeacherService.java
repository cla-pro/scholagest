package net.scholagest.services.impl;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.ITeacherService;

import com.google.inject.Inject;

public class TeacherService implements ITeacherService {
    private final IDatabase database;
    private final ITeacherBusinessComponent teacherBusinessComponent;
    private final IUserBusinessComponent userBusinessComponent;

    @Inject
    public TeacherService(IDatabase database, ITeacherBusinessComponent teacherBusinessComponent, IUserBusinessComponent userBusinessComponent) {
        this.database = database;
        this.teacherBusinessComponent = teacherBusinessComponent;
        this.userBusinessComponent = userBusinessComponent;
    }

    @Override
    public BaseObject createTeacher(String requestId, String teacherType, Map<String, Object> teacherProperties) throws Exception {
        BaseObject teacher = null;

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            teacher = teacherBusinessComponent.createTeacher(requestId, transaction, teacherType, teacherProperties);
            userBusinessComponent.createUser(requestId, transaction, teacher.getKey());
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
