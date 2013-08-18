package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.ClassObject;
import net.scholagest.objects.ObjectHelper;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class ClassManager extends ObjectManager implements IClassManager {
    @Inject
    public ClassManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public boolean checkWhetherClassExistsInYear(String className, String yearName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String classBasePropertyName = createClassesBasePropertyName(yearName, className);
        String classKey = (String) transaction.get(CoreNamespace.classesBase, classBasePropertyName, null);

        return classKey != null;
    }

    @Override
    public ClassObject createClass(String className, String yearName, Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String classKey = CoreNamespace.classNs + "/" + UUID.randomUUID().toString();

        DBSet students = DBSet.createDBSet(transaction, generateClassStudentsKey(classKey));
        DBSet teachers = DBSet.createDBSet(transaction, generateClassTeachersKey(classKey));
        DBSet branches = DBSet.createDBSet(transaction, generateClassBranchesKey(classKey));

        ClassObject clazz = new ClassObject(classKey);
        clazz.putAllProperties(properties);
        clazz.setBranches(branches);
        clazz.setStudents(students);
        clazz.setTeachers(teachers);

        persistObject(transaction, clazz);

        transaction.insert(CoreNamespace.classesBase, createClassesBasePropertyName(yearName, className), classKey, null);

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
    public void setClassProperties(String classKey, Map<String, Object> classProperties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        ClassObject clazz = new ClassObject(transaction, new ObjectHelper(getOntologyManager()), classKey);
        clazz.putAllProperties(classProperties);
    }

    @Override
    public ClassObject getClassProperties(String classKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new ClassObject(transaction, new ObjectHelper(getOntologyManager()), classKey);
    }

    @Override
    public Set<BaseObject> getClasses(String yearKey) {
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
