package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IClassManager {
    public BaseObject createClass(String className, String yearName) throws Exception;

    public Set<BaseObject> getClasses(String yearKey) throws Exception;

    public void setClassProperties(String classKey, Map<String, Object> classProperties) throws Exception;

    public BaseObject getClassProperties(String classKey, Set<String> properties) throws Exception;
}
