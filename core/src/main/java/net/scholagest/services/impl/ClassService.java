package net.scholagest.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IClassService;
import net.scholagest.services.kdom.KSet;
import net.scholagest.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ClassService implements IClassService {
    private final IDatabase database;
    private final IClassBusinessComponent classBusinessComponent;
    private final IUserBusinessComponent userBusinessComponent;
    private AuthorizationHelper authorizationHelper;

    @Inject
    public ClassService(IDatabase database, IClassBusinessComponent classBusinessComponent, IUserBusinessComponent userBusinessComponent,
            IOntologyBusinessComponent ontologyBusinessComponent) {
        this.database = database;
        this.classBusinessComponent = classBusinessComponent;
        this.userBusinessComponent = userBusinessComponent;
        this.authorizationHelper = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Override
    public BaseObject createClass(Map<String, Object> classProperties, String className, String yearKey) throws Exception {
        BaseObject clazz = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            clazz = classBusinessComponent.createClass(classProperties, className, yearKey);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return clazz;
    }

    @Override
    public Map<String, Set<BaseObject>> getClassesForYears(Set<String> yearKeySet) throws Exception {
        Map<String, Set<BaseObject>> classes = new HashMap<>();

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            classes = classBusinessComponent.getClassesForYears(yearKeySet);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return classes;
    }

    @Override
    public BaseObject getClassProperties(String classKey, Set<String> propertiesName) throws Exception {
        BaseObject classInfo = null;

        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            classInfo = classBusinessComponent.getClassProperties(classKey, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return classInfo;
    }

    @Override
    public void setClassProperties(String classKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            BaseObject oldClass = classBusinessComponent.getClassProperties(classKey,
                    new HashSet<String>(Arrays.asList(CoreNamespace.pClassStudents, CoreNamespace.pClassTeachers)));

            classBusinessComponent.setClassProperties(classKey, properties);

            BaseObject newClass = classBusinessComponent.getClassProperties(classKey,
                    new HashSet<String>(Arrays.asList(CoreNamespace.pClassStudents, CoreNamespace.pClassTeachers)));
            updateUserAuthorizations(oldClass.getProperties(), newClass.getProperties());

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private void updateUserAuthorizations(Map<String, Object> oldProperties, Map<String, Object> newProperties) throws Exception {
        removeUserAuthorizations(oldProperties);
        addUserAuthorizations(newProperties);
    }

    private void removeUserAuthorizations(Map<String, Object> oldProperties) throws Exception {
        KSet teachers = (KSet) oldProperties.get(CoreNamespace.pClassTeachers);
        KSet students = (KSet) oldProperties.get(CoreNamespace.pClassStudents);

        for (Object teacherKey : teachers.getValues()) {
            userBusinessComponent.removeUsersPermissions((String) teacherKey, students);
        }
    }

    private void addUserAuthorizations(Map<String, Object> newProperties) throws Exception {
        KSet teachers = (KSet) newProperties.get(CoreNamespace.pClassTeachers);
        KSet students = (KSet) newProperties.get(CoreNamespace.pClassStudents);

        for (Object teacherKey : teachers.getValues()) {
            userBusinessComponent.addUsersPermissions((String) teacherKey, students);
        }
    }
}
