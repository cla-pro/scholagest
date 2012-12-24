package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IStudentService {
    public BaseObject createStudent(String requestId, Map<String, Object> personalInfo) throws Exception;

    public void updateStudentProperties(String requestId, String studentKey, Map<String, Object> personalInfo, Map<String, Object> medicalInfo)
            throws Exception;

    public BaseObject getStudentPersonalProperties(String requestId, String studentKey, Set<String> properties) throws Exception;

    public BaseObject getStudentMedicalProperties(String requestId, String studentKey, Set<String> properties) throws Exception;

    public Set<BaseObject> getStudentsWithProperties(String requestId, Set<String> properties) throws Exception;
}
