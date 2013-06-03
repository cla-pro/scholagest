package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IClassBusinessComponent {
    public BaseObject createClass(Map<String, Object> classProperties) throws Exception;

    public Map<String, Set<BaseObject>> getClassesForYears(Set<String> yearKeySet) throws Exception;

    public BaseObject getClassProperties(String classKey, Set<String> propertiesName) throws Exception;

    public void setClassProperties(String classKey, Map<String, Object> classProperties) throws Exception;
}
