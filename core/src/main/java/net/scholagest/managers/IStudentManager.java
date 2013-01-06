package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IStudentManager {
    public BaseObject createStudent(String requestId, ITransaction transaction) throws Exception;

    public void setPersonalProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> properties) throws Exception;

    public void setMedicalProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> properties) throws Exception;

    public Set<BaseObject> getStudents(String requestId, ITransaction transaction) throws Exception;

    public BaseObject getPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception;

    public BaseObject getMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception;

    public BaseObject getStudentProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception;
}
