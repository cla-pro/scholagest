package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IStudentBusinessComponent {
    public String createStudent(String requestId, ITransaction transaction, Map<String, Object> personalInfo) throws Exception;

    public void updateStudentProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> personalProperties,
            Map<String, Object> medicalProperties) throws Exception;

    public Map<String, Object> getStudentPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception;

    public Map<String, Object> getStudentMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception;

    public Map<String, Map<String, Object>> getStudentsWithProperties(String requestId, ITransaction transaction, Set<String> properties)
            throws Exception;
}
