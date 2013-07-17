package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IStudentBusinessComponent {
    public BaseObject createStudent(Map<String, Object> personalInfo) throws Exception;

    public void updateStudentProperties(String studentKey, Map<String, Object> personalProperties, Map<String, Object> medicalProperties)
            throws Exception;

    public BaseObject getStudentPersonalProperties(String studentKey, Set<String> properties) throws Exception;

    public BaseObject getStudentMedicalProperties(String studentKey, Set<String> properties) throws Exception;

    public Set<BaseObject> getStudentsWithProperties(Set<String> properties) throws Exception;

    public BaseObject getStudentProperties(String studentKey, Set<String> properties) throws Exception;

    public Map<String, Map<String, BaseObject>> getGrades(Set<String> studentKeys, Set<String> examKeys, String yearKey) throws Exception;

    public void setStudentGrades(String studentKey, Map<String, BaseObject> studentGrades, String yearKey, String classKey, String branchKey,
            String periodKey) throws Exception;
}
