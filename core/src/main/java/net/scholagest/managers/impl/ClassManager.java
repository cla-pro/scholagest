package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ClassManager extends ObjectManager implements IClassManager {
    @Inject
    public ClassManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createClass(String className, String yearName) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String classKey = CoreNamespace.classNs + "/" + yearName + "#" + className;

        BaseObject clazz = super.createObject(transaction, classKey, CoreNamespace.tClass);

        transaction.insert(CoreNamespace.classesBase, createClassesBasePropertyName(yearName, className), classKey, null);

        String studentsSetKey = generateClassStudentsKey(classKey);
        DBSet.createDBSet(transaction, studentsSetKey);
        transaction.insert(classKey, CoreNamespace.pClassStudents, studentsSetKey, null);

        String teachersSetKey = generateClassTeachersKey(classKey);
        DBSet.createDBSet(transaction, teachersSetKey);
        transaction.insert(classKey, CoreNamespace.pClassTeachers, teachersSetKey, null);

        String branchesSetKey = generateClassBranchesKey(classKey);
        DBSet.createDBSet(transaction, branchesSetKey);
        transaction.insert(classKey, CoreNamespace.pClassBranches, branchesSetKey, null);

        return clazz;
    }

    private String generateClassStudentsKey(String classKey) {
        return classKey + "_students";
    }

    private String generateClassTeachersKey(String classKey) {
        return classKey + "_teachers";
    }

    private String generateClassBranchesKey(String classKey) {
        return classKey + "_branches";
    }

    private String createClassesBasePropertyName(String yearName, String className) {
        return yearName + "/" + className;
    }

    @Override
    public void setClassProperties(String classKey, Map<String, Object> classProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        super.setObjectProperties(transaction, classKey, classProperties);
    }

    @Override
    public BaseObject getClassProperties(String classKey, Set<String> properties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BaseObject clazz = new BaseObject(classKey, CoreNamespace.tClass);
        clazz.setProperties(super.getObjectProperties(transaction, classKey, properties));

        return clazz;
    }

    @Override
    public Set<BaseObject> getClasses(String yearKey) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        Set<BaseObject> classKeySet = new HashSet<>();
        for (String className : transaction.getColumns(CoreNamespace.classesBase)) {
            String classKey = (String) transaction.get(CoreNamespace.classesBase, className, null);
            String classYearKey = (String) transaction.get(classKey, CoreNamespace.pClassYear, null);
            if (yearKey == null || yearKey.equals(classYearKey)) {
                classKeySet.add(new BaseObject(classKey, CoreNamespace.tClass));
            }
        }

        return classKeySet;
    }
}
