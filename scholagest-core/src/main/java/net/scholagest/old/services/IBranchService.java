package net.scholagest.old.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.BaseObject;

public interface IBranchService {
    public BaseObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception;

    public BaseObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception;

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception;

    public Map<String, Map<String, BaseObject>> getBranchMeans(String branchKey, Set<String> studentKeys) throws Exception;
}
