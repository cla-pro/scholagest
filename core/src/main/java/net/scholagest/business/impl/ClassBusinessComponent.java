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
import net.scholagest.objects.ClassObject;

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
    public ClassObject createClass(Map<String, Object> classProperties, String className, String yearKey) {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        String yearName = (String) yearManager.getYearProperties(yearKey, properties).getProperty(CoreNamespace.pYearName);

        ClassObject clazz = classManager.createClass(className, yearName, classProperties);

        classProperties.put(CoreNamespace.pClassName, className);
        classProperties.put(CoreNamespace.pClassYear, yearKey);
        classManager.setClassProperties(clazz.getKey(), classProperties);

        yearManager.addClassToYear(yearKey, clazz.getKey());

        return clazz;
    }

    @Override
    public Map<String, Set<BaseObject>> getClassesForYears(Set<String> yearKeySet) {
        Map<String, Set<BaseObject>> classes = new HashMap<>();

        for (String yearKey : yearKeySet) {
            classes.put(yearKey, classManager.getClasses(yearKey));
        }

        return classes;
    }

    @Override
    public ClassObject getClassProperties(String classKey, Set<String> propertiesName) {
        return classManager.getClassProperties(classKey, propertiesName);
    }

    @Override
    public void setClassProperties(String classKey, Map<String, Object> classProperties) {
        classManager.setClassProperties(classKey, classProperties);
    }
}
