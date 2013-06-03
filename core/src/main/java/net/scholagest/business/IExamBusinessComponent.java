package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IExamBusinessComponent {
    public BaseObject createExam(String yearKey, String classKey, String branchKey, String periodKey, Map<String, Object> examInfo) throws Exception;

    public BaseObject getExamProperties(String examKey, Set<String> propertiesName) throws Exception;
}
