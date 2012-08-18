package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IStudentService {
    public String createStudent(String requestId, Map<String, Object> personalInfo) throws Exception;
    
    public void updateStudentInfo(String requestId, String studentKey,
            Map<String, Object> personalInfo,
            Map<String, Object> medicalInfo) throws Exception;
    
    public Map<String, Object> getStudentInfo(String requestId, String studentKey,
            Set<String> properties) throws Exception;
}
