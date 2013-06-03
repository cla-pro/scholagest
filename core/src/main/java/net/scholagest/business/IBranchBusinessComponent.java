package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IBranchBusinessComponent {
    public BaseObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception;

    public BaseObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception;

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception;
}
