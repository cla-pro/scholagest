package net.scholagest.business;

import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IYearBusinessComponent {
    public BaseObject startYear(String requestId, ITransaction transaction, String yearName) throws Exception;

    public void stopYear(String requestId, ITransaction transaction) throws Exception;

    public BaseObject getCurrentYearKey(String requestId, ITransaction transaction) throws Exception;

    public Set<BaseObject> getYearsWithProperties(String requestId, ITransaction transaction, Set<String> properties) throws Exception;
}
