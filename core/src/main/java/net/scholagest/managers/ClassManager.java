package net.scholagest.managers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.types.DBSet;

import com.google.inject.Inject;

public class ClassManager extends ObjectManager implements IClassManager {
    @Inject
    public ClassManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public String createClass(String requestId, ITransaction transaction, String className, String yearName) throws Exception {
        String classKey = CoreNamespace.classNs + "/" + yearName + "#" + className;

        super.createObject(requestId, transaction, classKey, CoreNamespace.tClass);

        transaction.insert(CoreNamespace.classesBase, createClassesBasePropertyName(yearName, className), classKey, null);

        String studentsSetKey = generateClassStudentsKey(classKey);
        DBSet.createDBSet(transaction, studentsSetKey);
        transaction.insert(classKey, CoreNamespace.pYearClasses, studentsSetKey, null);

        String teachersSetKey = generateClassTeachersKey(classKey);
        DBSet.createDBSet(transaction, teachersSetKey);
        transaction.insert(classKey, CoreNamespace.pYearClasses, teachersSetKey, null);

        return classKey;
    }

    private String generateClassStudentsKey(String classKey) {
        return classKey + "_students";
    }

    private String generateClassTeachersKey(String classKey) {
        return classKey + "_teachers";
    }

    private String createClassesBasePropertyName(String yearName, String className) {
        return yearName + "/" + className;
    }

    @Override
    public void setClassProperties(String requestId, ITransaction transaction, String classKey, Map<String, Object> classProperties) throws Exception {
        super.setObjectProperties(requestId, transaction, classKey, classProperties);
    }

    @Override
    public Map<String, Object> getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> properties)
            throws Exception {
        return super.getObjectProperties(requestId, transaction, classKey, properties);
    }

    @Override
    public Set<String> getClasses(String requestId, ITransaction transaction, String yearKey) throws Exception {
        Set<String> classKeySet = new HashSet<>();
        for (String className : transaction.getColumns(CoreNamespace.classesBase)) {
            String classKey = (String) transaction.get(CoreNamespace.classesBase, className, null);
            String classYearKey = (String) transaction.get(classKey, CoreNamespace.pClassYear, null);
            if (yearKey == null || yearKey.equals(classYearKey)) {
                classKeySet.add(classKey);
            }
        }

        return classKeySet;
    }
}
