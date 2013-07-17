package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.ExamObject;

public interface IExamManager {
    public ExamObject createExam(String examName, String classKey, String periodName, String branchName, String className, String yearName)
            throws Exception;

    public void setExamProperties(String key, Map<String, Object> examProperties) throws Exception;

    public ExamObject getExamProperties(String examKey, Set<String> properties) throws Exception;
}
