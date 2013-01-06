package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class StudentService implements IStudentService {
    private IDatabase database = null;
    private IStudentBusinessComponent studentBusinessComponent;

    @Inject
    public StudentService(IDatabase database, IStudentBusinessComponent studentBusinessComponent) {
        this.database = database;
        this.studentBusinessComponent = studentBusinessComponent;
    }

    @Override
    public BaseObject createStudent(String requestId, Map<String, Object> personalProperties) throws Exception {
        BaseObject student = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            student = studentBusinessComponent.createStudent(requestId, transaction, personalProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return student;
    }

    @Override
    public void updateStudentProperties(String requestId, String studentKey, Map<String, Object> personalProperties,
            Map<String, Object> medicalProperties) throws Exception {
        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            studentBusinessComponent.updateStudentProperties(requestId, transaction, studentKey, personalProperties, medicalProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public BaseObject getStudentPersonalProperties(String requestId, String studentKey, Set<String> properties) throws Exception {
        BaseObject personalProperties = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            personalProperties = studentBusinessComponent.getStudentPersonalProperties(requestId, transaction, studentKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return personalProperties;
    }

    @Override
    public BaseObject getStudentMedicalProperties(String requestId, String studentKey, Set<String> properties) throws Exception {
        BaseObject medicalProperties = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            medicalProperties = studentBusinessComponent.getStudentMedicalProperties(requestId, transaction, studentKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return medicalProperties;
    }

    @Override
    public Set<BaseObject> getStudentsWithProperties(String requestId, Set<String> properties) throws Exception {
        Set<BaseObject> students = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            students = studentBusinessComponent.getStudentsWithProperties(requestId, transaction, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return students;
    }

    @Override
    public BaseObject getStudentProperties(String requestId, String studentKey, Set<String> properties) throws Exception {
        BaseObject studentObject = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            studentObject = studentBusinessComponent.getStudentProperties(requestId, transaction, studentKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return studentObject;
    }
}
