package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IClassBusinessComponent {
    public BaseObject createClass(String requestId, ITransaction transaction, Map<String, Object> classProperties) throws Exception;

    public Map<String, Set<BaseObject>> getClassesForYears(String requestId, ITransaction transaction, Set<String> yearKeySet) throws Exception;

    public BaseObject getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> propertiesName) throws Exception;

    public void setClassProperties(String requestId, ITransaction transaction, String classKey, Map<String, Object> classProperties) throws Exception;
}
