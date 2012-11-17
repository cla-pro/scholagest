package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IYearManager {
    public String createNewYear(String requestId, ITransaction transaction, String yearName) throws Exception;

    public void restoreYear(String requestId, ITransaction transaction, String yearKey) throws Exception;

    public void backupYear(String requestId, ITransaction transaction) throws Exception;

    public String getCurrentYearKey(String requestId, ITransaction transaction) throws Exception;

    public Set<String> getYears(String requestId, ITransaction transaction) throws Exception;

    public Map<String, Object> getYearProperties(String requestId, ITransaction transaction, String yearKey, Set<String> propertiesName)
            throws Exception;

    public void addClassToYear(String requestId, ITransaction transaction, String yearKey, String classKey) throws Exception;
}
