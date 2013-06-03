package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IStudentManager {
    public BaseObject createStudent() throws Exception;

    public void setPersonalProperties(String studentKey, Map<String, Object> properties) throws Exception;

    public void setMedicalProperties(String studentKey, Map<String, Object> properties) throws Exception;

    public Set<BaseObject> getStudents() throws Exception;

    public BaseObject getPersonalProperties(String studentKey, Set<String> properties) throws Exception;

    public BaseObject getMedicalProperties(String studentKey, Set<String> properties) throws Exception;

    public BaseObject getStudentProperties(String studentKey, Set<String> properties) throws Exception;

    public Map<String, BaseObject> getStudentGrades(String studentKey, Set<String> examKeys, String yearKey) throws Exception;

    public void persistStudentGrade(String studentKey, String yearKey, String examKey, BaseObject gradeObject) throws Exception;
}
