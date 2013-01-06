package net.scholagest.managers;

import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IYearManager {
    public BaseObject createNewYear(String requestId, ITransaction transaction, String yearName) throws Exception;

    public void restoreYear(String requestId, ITransaction transaction, String yearKey) throws Exception;

    public void backupYear(String requestId, ITransaction transaction) throws Exception;

    public BaseObject getCurrentYearKey(String requestId, ITransaction transaction) throws Exception;

    public Set<BaseObject> getYears(String requestId, ITransaction transaction) throws Exception;

    public BaseObject getYearProperties(String requestId, ITransaction transaction, String yearKey, Set<String> propertiesName) throws Exception;

    public void addClassToYear(String requestId, ITransaction transaction, String yearKey, String classKey) throws Exception;
}
