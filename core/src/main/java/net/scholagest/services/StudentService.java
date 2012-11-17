package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;

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
    public String createStudent(String requestId, Map<String, Object> personalProperties) throws Exception {
        String studentKey = null;

        ITransaction transaction = this.database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        try {
            studentKey = studentBusinessComponent.createStudent(requestId, transaction, personalProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            e.printStackTrace();
        }

        return studentKey;
    }

    @Override
    public void updateStudentProperties(String requestId, String studentKey, Map<String, Object> personalProperties, Map<String, Object> medicalProperties)
            throws Exception {
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
    public Map<String, Object> getStudentPersonalProperties(String requestId, String studentKey, Set<String> properties) throws Exception {
        Map<String, Object> personalProperties = null;

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
    public Map<String, Object> getStudentMedicalProperties(String requestId, String studentKey, Set<String> properties) throws Exception {
        Map<String, Object> medicalProperties = null;

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
    public Map<String, Map<String, Object>> getStudentsWithProperties(String requestId, Set<String> properties) throws Exception {
        Map<String, Map<String, Object>> students = null;

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
}
