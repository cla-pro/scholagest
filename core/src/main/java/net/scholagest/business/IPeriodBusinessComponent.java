package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IPeriodBusinessComponent {
    public void setPeriodProperties(String requestId, ITransaction transaction, String periodKey, Map<String, Object> periodProperties)
            throws Exception;

    public BaseObject getPeriodProperties(String requestId, ITransaction transaction, String periodKey, Set<String> properties) throws Exception;
}
