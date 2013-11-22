package net.scholagest.services.impl;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.TeacherObject;
import net.scholagest.objects.UserObject;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.kdom.DBToKdomConverter;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class TeacherService implements ITeacherService {
    private final IDatabase database;
    private final ITeacherBusinessComponent teacherBusinessComponent;
    private final IUserBusinessComponent userBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public TeacherService(IDatabase database, ITeacherBusinessComponent teacherBusinessComponent, IUserBusinessComponent userBusinessComponent,
            IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.teacherBusinessComponent = teacherBusinessComponent;
        this.userBusinessComponent = userBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public BaseObject createTeacher(String teacherType, Map<String, Object> teacherProperties) throws Exception {
        BaseObject teacher = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            BaseObject dbTeacher = teacherBusinessComponent.createTeacher(teacherType, teacherProperties);
            String teacherKey = dbTeacher.getKey();

            UserObject userObject = userBusinessComponent.createUser(teacherKey);
            userObject.getRoles().add(convertTeacherTypeToRole(teacherType));
            userObject.getPermissions().add(teacherKey);

            teacher = new DBToKdomConverter().convertDbToKdom(dbTeacher, null);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teacher;
    }

    private String convertTeacherTypeToRole(String teacherType) {
        if (teacherType == null) {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.INVALID_TEACHER_TYPE, "The teacher type cannot be null");
        }

        if (teacherType.equals(AuthorizationRolesNamespace.ROLE_ADMIN)) {
            return AuthorizationRolesNamespace.ROLE_ADMIN;
        } else if (teacherType.equals(AuthorizationRolesNamespace.ROLE_TEACHER)) {
            return AuthorizationRolesNamespace.ROLE_TEACHER;
        } else if (teacherType.equals(AuthorizationRolesNamespace.ROLE_HELP_TEACHER)) {
            return AuthorizationRolesNamespace.ROLE_HELP_TEACHER;
        } else {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.INVALID_TEACHER_TYPE, "The teacher type \"" + teacherType
                    + "\" is invalid");
        }
    }

    @Override
    public Set<BaseObject> getTeachers() throws Exception {
        Set<BaseObject> teachers = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            Set<TeacherObject> dbTeachers = teacherBusinessComponent.getTeachers();
            teachers = new DBToKdomConverter().convertDbSetToKdom(dbTeachers, null);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teachers;
    }

    @Override
    public Set<BaseObject> getTeachersWithProperties(Set<String> propertyNames) throws Exception {
        Set<BaseObject> teachers = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());
            // Roles:
            // - All data: Admin, teacher itself
            // - TODO Without restricted data: other teachers

            Set<TeacherObject> dbTeachers = teacherBusinessComponent.getTeachersWithProperties(propertyNames);
            teachers = new DBToKdomConverter().convertDbSetToKdom(dbTeachers, propertyNames);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teachers;
    }

    @Override
    public void setTeacherProperties(String teacherKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(teacherKey));

            teacherBusinessComponent.setTeacherProperties(teacherKey, properties);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    @Override
    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertyNames) throws Exception {
        BaseObject teacher = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());
            // Roles:
            // - All data: Admin, teacher itself
            // - TODO Without restricted data: other teachers

            BaseObject dbTeacher = teacherBusinessComponent.getTeacherProperties(teacherKey, propertyNames);
            teacher = new DBToKdomConverter().convertDbToKdom(dbTeacher, propertyNames);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return teacher;
    }
}
