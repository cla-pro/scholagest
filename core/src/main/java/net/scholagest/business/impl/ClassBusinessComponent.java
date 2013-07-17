package net.scholagest.business.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.namespace.CoreNamespace;
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
    public BaseObject createClass(Map<String, Object> classProperties, String className, String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        String yearName = (String) yearManager.getYearProperties(yearKey, properties).getProperty(CoreNamespace.pYearName);

        BaseObject clazz = classManager.createClass(className, yearName);

        classProperties.put(CoreNamespace.pClassName, className);
        classProperties.put(CoreNamespace.pClassYear, yearKey);
        classManager.setClassProperties(clazz.getKey(), classProperties);

        yearManager.addClassToYear(yearKey, clazz.getKey());

        return clazz;
    }

    @Override
    public Map<String, Set<BaseObject>> getClassesForYears(Set<String> yearKeySet) throws Exception {
        Map<String, Set<BaseObject>> classes = new HashMap<>();

        for (String yearKey : yearKeySet) {
            classes.put(yearKey, classManager.getClasses(yearKey));
        }

        return classes;
    }

    @Override
    public BaseObject getClassProperties(String classKey, Set<String> propertiesName) throws Exception {
        return new DBToKdomConverter().convertDbToKdom(classManager.getClassProperties(classKey, propertiesName));
    }

    @Override
    public void setClassProperties(String classKey, Map<String, Object> classProperties) throws Exception {
        classManager.setClassProperties(classKey, classProperties);
    }
}
