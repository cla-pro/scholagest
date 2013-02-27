package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IPeriodManager {
    public BaseObject createPeriod(String requestId, ITransaction transaction, String periodName, String branchName, String className, String yearName)
            throws Exception;

    public void setPeriodProperties(String requestId, ITransaction transaction, String periodKey, Map<String, Object> periodProperties)
            throws Exception;

    public BaseObject getPeriodProperties(String requestId, ITransaction transaction, String periodKey, Set<String> properties) throws Exception;
}
