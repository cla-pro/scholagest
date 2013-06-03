package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IExamManager {
    public BaseObject createExam(String examName, String periodName, String branchName, String className, String yearName) throws Exception;

    public void setExamProperties(String key, Map<String, Object> examProperties) throws Exception;

    public BaseObject getExamProperties(String examKey, Set<String> properties) throws Exception;
}
