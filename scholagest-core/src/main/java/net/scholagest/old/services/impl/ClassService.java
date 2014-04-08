package net.scholagest.old.services.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.old.business.IClassBusinessComponent;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.business.IUserBusinessComponent;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.AuthorizationRolesNamespace;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.services.IClassService;
import net.scholagest.old.services.kdom.DBToKdomConverter;
import net.scholagest.old.shiro.AuthorizationHelper;
import net.scholagest.utils.ConfigurationService;
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

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            BaseObject dbClazz = classBusinessComponent.createClass(classProperties, className, yearKey);
            clazz = new DBToKdomConverter().convertDbToKdom(dbClazz, null);

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

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            Map<String, Set<BaseObject>> dbClasses = classBusinessComponent.getClassesForYears(yearKeySet);
            classes = convertToKdom(dbClasses);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return classes;
    }

    private Map<String, Set<BaseObject>> convertToKdom(Map<String, Set<BaseObject>> dbClasses) throws DatabaseException {
        Map<String, Set<BaseObject>> converted = new HashMap<>();

        DBToKdomConverter dbConvertor = new DBToKdomConverter();
        for (String classKey : dbClasses.keySet()) {
            converted.put(classKey, dbConvertor.convertDbSetToKdom(dbClasses.get(classKey), null));
        }

        return converted;
    }

    @Override
    public BaseObject getClassProperties(String classKey, Set<String> propertiesName) throws Exception {
        BaseObject clazz = null;

        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

            BaseObject dbClazz = classBusinessComponent.getClassProperties(classKey, propertiesName);
            clazz = new DBToKdomConverter().convertDbToKdom(dbClazz, propertiesName);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }

        return clazz;
    }

    @Override
    public void setClassProperties(String classKey, Map<String, Object> properties) throws Exception {
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            authorizationHelper.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());

            BaseObject oldClass = classBusinessComponent.getClassProperties(classKey,
                    new HashSet<String>(Arrays.asList(CoreNamespace.pClassStudents, CoreNamespace.pClassTeachers)));

            classBusinessComponent.setClassProperties(classKey, properties);

            BaseObject newClass = classBusinessComponent.getClassProperties(classKey,
                    new HashSet<String>(Arrays.asList(CoreNamespace.pClassStudents, CoreNamespace.pClassTeachers)));
            updateUserAuthorizations(oldClass.getProperties(), newClass.getProperties(), classKey);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            throw e;
        }
    }

    private void updateUserAuthorizations(Map<String, Object> oldProperties, Map<String, Object> newProperties, String classKey) throws Exception {
        removeUserAuthorizations(oldProperties);
        addUserAuthorizations(newProperties, classKey);
    }

    private void removeUserAuthorizations(Map<String, Object> oldProperties) throws Exception {
        DBSet teachers = (DBSet) oldProperties.get(CoreNamespace.pClassTeachers);
        DBSet students = (DBSet) oldProperties.get(CoreNamespace.pClassStudents);

        for (Object teacherKey : convertToSetString(teachers)) {
            userBusinessComponent.removeUsersPermissions((String) teacherKey, convertToSetString(students));
        }
    }

    private void addUserAuthorizations(Map<String, Object> newProperties, String classKey) throws Exception {
        DBSet teachers = (DBSet) newProperties.get(CoreNamespace.pClassTeachers);
        DBSet students = (DBSet) newProperties.get(CoreNamespace.pClassStudents);

        for (Object teacherKey : convertToSetString(teachers)) {
            userBusinessComponent.addUsersPermissions((String) teacherKey, new HashSet<String>(Arrays.asList(classKey)));
            userBusinessComponent.addUsersPermissions((String) teacherKey, convertToSetString(students));
        }
    }

    private Set<String> convertToSetString(DBSet students) {
        Set<String> converted = new HashSet<>();

        for (Object student : students.values()) {
            converted.add((String) student);
        }

        return converted;
    }
}
