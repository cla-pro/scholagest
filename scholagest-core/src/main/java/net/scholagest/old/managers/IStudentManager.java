package net.scholagest.old.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.StudentObject;

public interface IStudentManager {
    public StudentObject createStudent(Map<String, Object> personalProperties);

    public void setPersonalProperties(String studentKey, Map<String, Object> properties);

    public void setMedicalProperties(String studentKey, Map<String, Object> properties);

    public Set<StudentObject> getStudents();

    public BaseObject getPersonalProperties(String studentKey, Set<String> properties);

    public BaseObject getMedicalProperties(String studentKey, Set<String> properties);

    public StudentObject getStudentProperties(String studentKey, Set<String> properties);

    public Map<String, BaseObject> getStudentGrades(String studentKey, Set<String> examKeys, String yearKey);

    public void persistStudentGrade(String studentKey, String yearKey, String examKey, BaseObject gradeObject);
}
