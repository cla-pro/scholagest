package net.scholagest.old.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.ClassObject;

public interface IClassManager {
    public ClassObject createClass(String className, String yearName, Map<String, Object> properties);

    public Set<BaseObject> getClasses(String yearKey);

    public void setClassProperties(String classKey, Map<String, Object> classProperties);

    public ClassObject getClassProperties(String classKey, Set<String> properties);

    public boolean checkWhetherClassExistsInYear(String className, String yearName);
}
