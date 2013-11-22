package net.scholagest.business.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.IClassBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.ClassObject;
import net.scholagest.objects.TeacherObject;

import com.google.inject.Inject;

public class ClassBusinessComponent implements IClassBusinessComponent {
    private IClassManager classManager;
    private IYearManager yearManager;
    private ITeacherManager teacherManager;

    @Inject
    public ClassBusinessComponent(IClassManager classManager, IYearManager yearManager, ITeacherManager teacherManager) {
        this.classManager = classManager;
        this.yearManager = yearManager;
        this.teacherManager = teacherManager;
    }

    @Override
    public ClassObject createClass(Map<String, Object> classProperties, String className, String yearKey) throws ScholagestException {
        BaseObject yearObject = yearManager.getYearProperties(yearKey, new HashSet<String>());
        String yearName = (String) yearObject.getProperty(CoreNamespace.pYearName);

        if (classManager.checkWhetherClassExistsInYear(className, yearName)) {
            throw new ScholagestException(ScholagestExceptionErrorCode.OBJECT_ALREADY_EXISTS, "A class with the same name " + className
                    + " already exists for year=" + yearKey);
        }

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
        ClassObject oldClassObject = getClassProperties(classKey, new HashSet<String>());
        Set<String> oldTeachers = oldClassObject.getTeachers().values();

        classManager.setClassProperties(classKey, classProperties);

        ClassObject newClassObject = getClassProperties(classKey, new HashSet<String>());
        Set<String> newTeachers = newClassObject.getTeachers().values();

        for (String teacherKey : oldTeachers) {
            TeacherObject teacherObject = teacherManager.getTeacherProperties(teacherKey, new HashSet<String>());
            DBSet classes = (DBSet) teacherObject.getProperty(CoreNamespace.pTeacherClasses);
            classes.remove(classKey);
        }

        for (String teacherKey : newTeachers) {
            TeacherObject teacherObject = teacherManager.getTeacherProperties(teacherKey, new HashSet<String>());
            DBSet classes = (DBSet) teacherObject.getProperty(CoreNamespace.pTeacherClasses);
            classes.add(classKey);
        }
    }
}
