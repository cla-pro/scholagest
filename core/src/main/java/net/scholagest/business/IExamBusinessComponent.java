package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.ExamObject;

public interface IExamBusinessComponent {
    public ExamObject createExam(String yearKey, String classKey, String branchKey, String periodKey, Map<String, Object> examInfo) throws Exception;

    public ExamObject getExamProperties(String examKey, Set<String> propertiesName) throws Exception;
}
