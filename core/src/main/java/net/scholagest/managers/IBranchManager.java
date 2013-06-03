package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IBranchManager {
    public BaseObject createBranch(String branchName, String className, String yearName) throws Exception;

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception;

    public BaseObject getBranchProperties(String branchKey, Set<String> properties) throws Exception;
}
