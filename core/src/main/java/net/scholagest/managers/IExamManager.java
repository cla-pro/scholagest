package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IExamManager {
    public BaseObject createExam(String requestId, ITransaction transaction, String examName, String periodName, String branchName, String className,
            String yearName) throws Exception;

    public void setExamProperties(String requestId, ITransaction transaction, String key, Map<String, Object> examProperties) throws Exception;

    public BaseObject getExamProperties(String requestId, ITransaction transaction, String examKey, Set<String> properties) throws Exception;
}
