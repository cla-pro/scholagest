package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface ITeacherBusinessComponent {
    public Set<String> getTeacherTypes() throws Exception;

    public BaseObject createTeacher(String teacherType, Map<String, Object> teacherProperties) throws Exception;

    public Set<BaseObject> getTeachers() throws Exception;

    public Set<BaseObject> getTeachersWithProperties(Set<String> propertiesName) throws Exception;

    public Set<BaseObject> getTeacherClasses(String teacherKey) throws Exception;

    public void setTeacherProperties(String teacherKey, Map<String, Object> properties) throws Exception;

    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertiesName) throws Exception;
}
