package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IYearBusinessComponent {
    public String startYear(String requestId, ITransaction transaction, String yearName) throws Exception;

    public void stopYear(String requestId, ITransaction transaction) throws Exception;

    public String getCurrentYearKey(String requestId, ITransaction transaction) throws Exception;

    public Map<String, Map<String, Object>> getYearsWithProperties(String requestId, ITransaction transaction, Set<String> properties)
            throws Exception;

}
