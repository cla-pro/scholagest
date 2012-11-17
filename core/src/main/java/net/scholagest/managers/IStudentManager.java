package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IStudentManager {
    public String createStudent(String requestId, ITransaction transaction) throws Exception;

    public void setPersonalProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> properties)
            throws Exception;

    public void setMedicalProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> properties)
            throws Exception;

    public Set<String> getStudents(String requestId, ITransaction transaction) throws Exception;

    public Map<String, Object> getPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception;

    public Map<String, Object> getMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception;
}
