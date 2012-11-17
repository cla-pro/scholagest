package net.scholagest.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IYearManager;

import com.google.inject.Inject;

public class ClassBusinessComponent implements IClassBusinessComponent {
    private IClassManager classManager;
    private IYearManager yearManager;

    @Inject
    public ClassBusinessComponent(IClassManager classManager, IYearManager yearManager) {
        this.classManager = classManager;
        this.yearManager = yearManager;
    }

    @Override
    public String createClass(String requestId, ITransaction transaction, Map<String, Object> classProperties) throws Exception {
        String yearKey = (String) classProperties.get(CoreNamespace.pClassYear);

        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        String yearName = (String) yearManager.getYearProperties(requestId, transaction, yearKey, properties).get(CoreNamespace.pYearName);

        String classKey = classManager.createClass(requestId, transaction, (String) classProperties.get(CoreNamespace.pClassName), yearName);
        classManager.setClassProperties(requestId, transaction, classKey, classProperties);

        yearManager.addClassToYear(requestId, transaction, yearKey, classKey);

        return classKey;
    }

    @Override
    public Map<String, Set<String>> getClassesForYear(String requestId, ITransaction transaction, Set<String> yearKeySet) throws Exception {
        Map<String, Set<String>> classesKey = new HashMap<>();

        for (String yearKey : yearKeySet) {
            classesKey.put(yearKey, classManager.getClasses(requestId, transaction, yearKey));
        }

        return classesKey;
    }

    @Override
    public Map<String, Object> getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> propertiesName)
            throws Exception {
        return classManager.getClassProperties(requestId, transaction, classKey, propertiesName);
    }
}
