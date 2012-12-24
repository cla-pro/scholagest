package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface ITeacherService {
    // public Set<String> getTeacherTypes() throws Exception;

    public BaseObject createTeacher(String requestId, String teacherType, Map<String, Object> teacherProperties) throws Exception;

    public Set<BaseObject> getTeachers(String requestId) throws Exception;

    public Set<BaseObject> getTeachersWithProperties(String requestId, Set<String> propertiesName) throws Exception;

    // Multiple year at once?
    // public Set<String> getTeacherClasses(String requestId, String teacherKey)
    // throws Exception;

    public void setTeacherProperties(String requestId, String teacherKey, Map<String, Object> properties) throws Exception;

    public BaseObject getTeacherProperties(String requestId, String teacherKey, Set<String> propertiesName) throws Exception;
}
