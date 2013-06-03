package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IClassService {
    // Create a new class into the current year. Throws an exception if no year
    // is currently running or if a class with the same name already exists.
    public BaseObject createClass(Map<String, Object> classProperties) throws Exception;

    public Map<String, Set<BaseObject>> getClassesForYears(Set<String> yearKeyList) throws Exception;

    public BaseObject getClassProperties(String classKey, Set<String> propertiesName) throws Exception;

    public void setClassProperties(String classKey, Map<String, Object> properties) throws Exception;
}
