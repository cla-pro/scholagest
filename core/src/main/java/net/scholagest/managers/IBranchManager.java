package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IBranchManager {
    public BaseObject createBranch(String requestId, ITransaction transaction, String branchName, String className, String yearName) throws Exception;

    public void setBranchProperties(String requestId, ITransaction transaction, String branchKey, Map<String, Object> branchProperties)
            throws Exception;

    public BaseObject getBranchProperties(String requestId, ITransaction transaction, String branchKey, Set<String> properties) throws Exception;
}
