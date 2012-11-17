package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IClassManager {
    public String createClass(String requestId, ITransaction transaction, String className, String yearName) throws Exception;

    public Set<String> getClasses(String requestId, ITransaction transaction, String yearKey) throws Exception;

    public void setClassProperties(String requestId, ITransaction transaction, String classKey, Map<String, Object> classProperties) throws Exception;

    public Map<String, Object> getClassProperties(String requestId, ITransaction transaction, String classKey, Set<String> properties)
            throws Exception;
}
