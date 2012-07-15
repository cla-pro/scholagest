package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IStudentService {
    public String createStudent(String requestId, ITransaction transaction,
            Map<String, Object> personalInfo) throws Exception;
    
    public void updateStudentInfo(String studentKey,
            Map<String, Object> personalInfo,
            Map<String, Object> medicalInfo) throws Exception;
    
    public Map<String, Object> getStudentInfo(String studentKey,
            Set<String> properties) throws Exception;
    
    public Set<String> searchStudent(String requestId, ITransaction transaction
            /* Parameters to define */) throws Exception;
}
