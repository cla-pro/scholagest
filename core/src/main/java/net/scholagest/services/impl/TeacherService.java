package net.scholagest.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.UserObject;
import net.scholagest.services.ITeacherService;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

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
    public BaseObject createTeacher(String teacherType, Map<String, Object> teacherProperties) throws Exception {
        BaseObject teacher = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAdminRole());

            teacher = teacherBusinessComponent.createTeacher(teacherType, teacherProperties);

            UserObject userObject = userBusinessComponent.createUser(teacher.getKey());
            userObject.getRoles().add(AuthorizationNamespace.ROLE_TEACHER);
            userObject.getPermissions().add(teacher.getKey());

            Map<String, Object> userProperty = new HashMap<String, Object>();
            userProperty.put(CoreNamespace.pTeacherUser, userObject.getKey());
            teacherBusinessComponent.setTeacherProperties(teacher.getKey(), userProperty);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teacher;
    }

    @Override
    public Set<BaseObject> getTeachers() throws Exception {
        Set<BaseObject> teachers = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());

            teachers = teacherBusinessComponent.getTeachers();
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teachers;
    }

    @Override
    public Set<BaseObject> getTeachersWithProperties(Set<String> propertiesName) throws Exception {
        Set<BaseObject> teachers = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());
            // Roles:
            // - All data: Admin, teacher itself
            // - TODO Without restricted data: other teachers

            teachers = teacherBusinessComponent.getTeachersWithProperties(propertiesName);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teachers;
    }

    @Override
    public void setTeacherProperties(String teacherKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorization(AuthorizationNamespace.getAdminRole(), Arrays.asList(teacherKey));
            // TODO Roles: Admin, teacher itself

            teacherBusinessComponent.setTeacherProperties(teacherKey, properties);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertiesName) throws Exception {
        BaseObject properties = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            new AuthorizationHelper().checkAuthorizationRoles(AuthorizationNamespace.getAllRoles());
            // Roles:
            // - All data: Admin, teacher itself
            // - TODO Without restricted data: other teachers

            properties = teacherBusinessComponent.getTeacherProperties(teacherKey, propertiesName);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return properties;
    }
}
