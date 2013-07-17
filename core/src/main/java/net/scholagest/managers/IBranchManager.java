package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BranchObject;

public interface IBranchManager {
    public BranchObject createBranch(String branchName, String className, String yearName, Map<String, Object> properties) throws Exception;

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception;

    public BranchObject getBranchProperties(String branchKey, Set<String> properties) throws Exception;
}
