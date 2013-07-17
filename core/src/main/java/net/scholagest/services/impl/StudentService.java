package net.scholagest.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IStudentService;
import net.scholagest.services.kdom.DBToKdomConverter;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class StudentService implements IStudentService {
    private IDatabase database = null;
    private IStudentBusinessComponent studentBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public StudentService(IDatabase database, IStudentBusinessComponent studentBusinessComponent, IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.studentBusinessComponent = studentBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public BaseObject createStudent(Map<String, Object> personalProperties) throws Exception {
        BaseObject student = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            BaseObject dbStudent = studentBusinessComponent.createStudent(personalProperties);
            student = new DBToKdomConverter().convertDbToKdom(dbStudent);

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
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(studentKey));

            studentBusinessComponent.updateStudentProperties(studentKey, personalProperties, medicalProperties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getStudentPersonalProperties(String studentKey, Set<String> properties) throws Exception {
        BaseObject personalInfo = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            BaseObject fullPersonalInfo = studentBusinessComponent.getStudentPersonalProperties(studentKey, properties);
            BaseObject dbPersonalInfo = authorizationHelper.filterObjectProperties(fullPersonalInfo, AuthorizationRolesNamespace.getAdminRole(),
                    Arrays.asList(studentKey));
            personalInfo = new DBToKdomConverter().convertDbToKdom(dbPersonalInfo);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return personalInfo;
    }

    @Override
    public BaseObject getStudentMedicalProperties(String studentKey, Set<String> properties) throws Exception {
        BaseObject medicalInfo = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            BaseObject fullMedicalInfo = studentBusinessComponent.getStudentMedicalProperties(studentKey, properties);
            BaseObject dbMedicalInfo = authorizationHelper.filterObjectProperties(fullMedicalInfo, AuthorizationRolesNamespace.getAdminRole(),
                    Arrays.asList(studentKey));
            medicalInfo = new DBToKdomConverter().convertDbToKdom(dbMedicalInfo);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return medicalInfo;
    }

    @Override
    public Set<BaseObject> getStudentsWithProperties(Set<String> properties) throws Exception {
        Set<BaseObject> students = new HashSet<>();

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            for (BaseObject dbFullStudent : studentBusinessComponent.getStudentsWithProperties(properties)) {
                BaseObject dbFilteredStudent = authorizationHelper.filterObjectProperties(dbFullStudent, AuthorizationRolesNamespace.getAdminRole(),
                        Arrays.asList(dbFullStudent.getKey()));
                BaseObject student = new DBToKdomConverter().convertDbToKdom(dbFilteredStudent);
                students.add(student);
            }

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return students;
    }

    @Override
    public BaseObject getStudentProperties(String studentKey, Set<String> properties) throws Exception {
        BaseObject student = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            BaseObject dbFullStudent = studentBusinessComponent.getStudentProperties(studentKey, properties);
            BaseObject dbFilteredStudent = authorizationHelper.filterObjectProperties(dbFullStudent, AuthorizationRolesNamespace.getAdminRole(),
                    Arrays.asList(studentKey));
            student = new DBToKdomConverter().convertDbToKdom(dbFilteredStudent);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return student;
    }

    @Override
    public Map<String, Map<String, BaseObject>> getGrades(Set<String> studentKeys, Set<String> examKeys, String yearKey) throws Exception {
        Map<String, Map<String, BaseObject>> grades = null;

        ITransaction transaction = this.database
                .getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), new ArrayList<>(studentKeys));

            grades = studentBusinessComponent.getGrades(studentKeys, examKeys, yearKey);
            // TODO convert grades

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
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(studentKey));

            studentBusinessComponent.setStudentGrades(studentKey, studentGrades, yearKey, classKey, branchKey, periodKey);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }
}
