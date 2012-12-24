package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IClassManager {
    public BaseObject createClass(String requestId, ITransaction transaction, String className, String yearName) throws Exception;

    public Set<BaseObject> getClasses(String requestId, ITransaction transaction, String yearKey) throws Exception;

    public void setClassProperties(String requestId, ITransaction transaction, String classKey, Map<String, Object> classProperties) throws Exception;

    public BaseObject getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> properties) throws Exception;
}
