package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.ExamObject;

public interface IExamManager {
    public ExamObject createExam(String examName, String classKey, String periodName, String branchName, String className, String yearName,
            Map<String, Object> properties);

    public void setExamProperties(String key, Map<String, Object> examProperties);

    public ExamObject getExamProperties(String examKey, Set<String> properties);

    public boolean checkWhetherExamExistsInPeriod(String examName, String yearName, String className, String branchName, String periodName);
}
