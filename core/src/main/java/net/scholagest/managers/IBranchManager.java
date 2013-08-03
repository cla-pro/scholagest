package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BranchObject;

public interface IBranchManager {
    public BranchObject createBranch(String branchName, String classKey, String className, String yearName, Map<String, Object> properties);

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties);

    public BranchObject getBranchProperties(String branchKey, Set<String> properties);
}
