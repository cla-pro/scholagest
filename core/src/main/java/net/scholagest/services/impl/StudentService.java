package net.scholagest.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IStudentService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

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
    public BaseObject createStudent(Map<String, Object> personalProperties) throws Exception {
        BaseObject student = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAdminRole());

            student = studentBusinessComponent.createStudent(personalProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return student;
    }

    @Override
    public void updateStudentProperties(String studentKey, Map<String, Object> personalProperties, Map<String, Object> medicalProperties)
            throws Exception {
        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(studentKey));

            studentBusinessComponent.updateStudentProperties(studentKey, personalProperties, medicalProperties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getStudentPersonalProperties(String studentKey, Set<String> properties) throws Exception {
        BaseObject personalProperties = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());
            // TODO Roles:
            // - All data: Admin + ClassTeacher
            // - Without restricted data: Other teachers and help teachers

            personalProperties = studentBusinessComponent.getStudentPersonalProperties(studentKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return personalProperties;
    }

    @Override
    public BaseObject getStudentMedicalProperties(String studentKey, Set<String> properties) throws Exception {
        BaseObject medicalProperties = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());
            // TODO Roles:
            // - All data: Admin + ClassTeacher
            // - Without restricted data: Other teachers and help teachers

            medicalProperties = studentBusinessComponent.getStudentMedicalProperties(studentKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return medicalProperties;
    }

    @Override
    public Set<BaseObject> getStudentsWithProperties(Set<String> properties) throws Exception {
        Set<BaseObject> students = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());
            // TODO Roles:
            // - All data: Admin + ClassTeacher
            // - Without restricted data: Other teachers and help teachers

            students = studentBusinessComponent.getStudentsWithProperties(properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return students;
    }

    @Override
    public BaseObject getStudentProperties(String studentKey, Set<String> properties) throws Exception {
        BaseObject studentObject = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());
            // TODO Roles:
            // - All data: Admin + ClassTeacher
            // - Without restricted data: Other teachers and help teachers

            studentObject = studentBusinessComponent.getStudentProperties(studentKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return studentObject;
    }

    @Override
    public Map<String, Map<String, BaseObject>> getGrades(Set<String> studentKeys, Set<String> examKeys, String yearKey) throws Exception {
        Map<String, Map<String, BaseObject>> grades = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), new ArrayList<>(studentKeys));

            grades = studentBusinessComponent.getGrades(studentKeys, examKeys, yearKey);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return grades;
    }

    @Override
    public void setGrades(String studentKey, Map<String, BaseObject> studentGrades, String yearKey, String classKey, String branchKey,
            String periodKey) throws Exception {
        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(studentKey));

            studentBusinessComponent.setStudentGrades(studentKey, studentGrades, yearKey, classKey, branchKey, periodKey);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
