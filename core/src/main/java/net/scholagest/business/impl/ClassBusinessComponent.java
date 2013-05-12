package net.scholagest.business.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.kdom.DBToKdomConverter;

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
    public BaseObject createClass(String requestId, ITransaction transaction, Map<String, Object> classProperties) throws Exception {
        String yearKey = (String) classProperties.get(CoreNamespace.pClassYear);

        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        String yearName = (String) yearManager.getYearProperties(requestId, transaction, yearKey, properties).getProperty(CoreNamespace.pYearName);

        BaseObject clazz = classManager.createClass(requestId, transaction, (String) classProperties.get(CoreNamespace.pClassName), yearName);
        classManager.setClassProperties(requestId, transaction, clazz.getKey(), classProperties);

        yearManager.addClassToYear(requestId, transaction, yearKey, clazz.getKey());

        return clazz;
    }

    @Override
    public Map<String, Set<BaseObject>> getClassesForYears(String requestId, ITransaction transaction, Set<String> yearKeySet) throws Exception {
        Map<String, Set<BaseObject>> classes = new HashMap<>();

        for (String yearKey : yearKeySet) {
            classes.put(yearKey, classManager.getClasses(requestId, transaction, yearKey));
        }

        return classes;
    }

    @Override
    public BaseObject getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> propertiesName) throws Exception {
        return new DBToKdomConverter().convertDbToKdom(classManager.getClassProperties(requestId, transaction, classKey, propertiesName));
    }

    @Override
    public void setClassProperties(String requestId, ITransaction transaction, String classKey, Map<String, Object> classProperties) throws Exception {
        classManager.setClassProperties(requestId, transaction, classKey, classProperties);
    }
}
