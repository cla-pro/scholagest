package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.TeacherObject;

public interface ITeacherManager {
    public TeacherObject createTeacher(Map<String, Object> teacherProperties);

    public Set<TeacherObject> getTeachers();

    public void setTeacherProperties(String teacherKey, Map<String, Object> teacherProperties);

    public TeacherObject getTeacherProperties(String teacherKey, Set<String> propertiesName);
}
