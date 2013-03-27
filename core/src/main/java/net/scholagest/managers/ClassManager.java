package net.scholagest.managers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class ClassManager extends ObjectManager implements IClassManager {
    @Inject
    public ClassManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createClass(String requestId, ITransaction transaction, String className, String yearName) throws Exception {
        String classKey = CoreNamespace.classNs + "/" + yearName + "#" + className;

        BaseObject clazz = super.createObject(requestId, transaction, classKey, CoreNamespace.tClass);

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
    public void setClassProperties(String requestId, ITransaction transaction, String classKey, Map<String, Object> classProperties) throws Exception {
        super.setObjectProperties(requestId, transaction, classKey, classProperties);
    }

    @Override
    public BaseObject getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> properties) throws Exception {
        BaseObject clazz = new BaseObject(classKey, CoreNamespace.tClass);
        clazz.setProperties(super.getObjectProperties(requestId, transaction, classKey, properties));

        return clazz;
    }

    @Override
    public Set<BaseObject> getClasses(String requestId, ITransaction transaction, String yearKey) throws Exception {
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
