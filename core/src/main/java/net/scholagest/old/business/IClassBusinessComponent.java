package net.scholagest.old.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.ClassObject;

public interface IClassBusinessComponent {
    public ClassObject createClass(Map<String, Object> classProperties, String className, String yearKey) throws ScholagestException;

    public Map<String, Set<BaseObject>> getClassesForYears(Set<String> yearKeySet);

    public ClassObject getClassProperties(String classKey, Set<String> propertiesName);

    public void setClassProperties(String classKey, Map<String, Object> classProperties);
}
