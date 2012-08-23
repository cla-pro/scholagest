package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IStudentService {
    public String createStudent(String requestId, Map<String, Object> personalInfo) throws Exception;
    
    public void updateStudentInfo(String requestId, String studentKey,
            Map<String, Object> personalInfo,
            Map<String, Object> medicalInfo) throws Exception;
    
    public Map<String, Object> getStudentPersonalInfo(String requestId, String studentKey,
            Set<String> properties) throws Exception;
    
    public Map<String, Object> getStudentMedicalInfo(String requestId, String studentKey,
            Set<String> properties) throws Exception;

	public Map<String, Map<String, Object>> getStudentsWithProperties(
			String requestId, Set<String> properties) throws Exception;
}
