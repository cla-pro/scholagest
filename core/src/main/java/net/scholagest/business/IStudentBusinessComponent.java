package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface IStudentBusinessComponent {
    public BaseObject createStudent(String requestId, ITransaction transaction, Map<String, Object> personalInfo) throws Exception;

    public void updateStudentProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> personalProperties,
            Map<String, Object> medicalProperties) throws Exception;

    public BaseObject getStudentPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception;

    public BaseObject getStudentMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception;

    public Set<BaseObject> getStudentsWithProperties(String requestId, ITransaction transaction, Set<String> properties) throws Exception;

    public BaseObject getStudentProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception;

    public Map<String, Map<String, BaseObject>> getGrades(String requestId, ITransaction transaction, Set<String> studentKeys, Set<String> examKeys,
            String yearKey) throws Exception;

    public void setStudentGrades(String requestId, ITransaction transaction, String studentKey, Map<String, BaseObject> studentGrades,
            String yearKey, String classKey, String branchKey, String periodKey) throws Exception;
}
