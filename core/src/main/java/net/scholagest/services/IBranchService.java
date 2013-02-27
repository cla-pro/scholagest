package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IBranchService {
    public BaseObject createBranch(String requestId, String classKey, Map<String, Object> branchProperties) throws Exception;

    public BaseObject getBranchProperties(String requestId, String branchKey, Set<String> propertiesName) throws Exception;

    public void setBranchProperties(String requestId, String branchKey, Map<String, Object> branchProperties) throws Exception;
}
