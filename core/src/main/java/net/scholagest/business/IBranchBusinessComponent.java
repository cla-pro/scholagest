package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BranchObject;

public interface IBranchBusinessComponent {
    public BranchObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception;

    public BranchObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception;

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception;

    public Map<String, Map<String, BaseObject>> getPeriodMeans(String branchKey, Set<String> studentKeys);
}
