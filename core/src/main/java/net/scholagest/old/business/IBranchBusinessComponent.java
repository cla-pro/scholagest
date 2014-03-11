package net.scholagest.old.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.BranchObject;

public interface IBranchBusinessComponent {
    public BranchObject createBranch(String classKey, Map<String, Object> branchProperties) throws Exception;

    public BranchObject getBranchProperties(String branchKey, Set<String> propertiesName) throws Exception;

    public void setBranchProperties(String branchKey, Map<String, Object> branchProperties) throws Exception;

    public Map<String, Map<String, BaseObject>> getPeriodMeans(String branchKey, Set<String> studentKeys);
}
