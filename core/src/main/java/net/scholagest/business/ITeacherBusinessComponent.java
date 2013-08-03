package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.TeacherObject;

public interface ITeacherBusinessComponent {
    public Set<String> getTeacherTypes();

    public TeacherObject createTeacher(String teacherType, Map<String, Object> teacherProperties);

    public Set<TeacherObject> getTeachers();

    public Set<TeacherObject> getTeachersWithProperties(Set<String> propertiesName);

    public Set<TeacherObject> getTeacherClasses(String teacherKey);

    public void setTeacherProperties(String teacherKey, Map<String, Object> properties);

    public TeacherObject getTeacherProperties(String teacherKey, Set<String> propertiesName);
}
