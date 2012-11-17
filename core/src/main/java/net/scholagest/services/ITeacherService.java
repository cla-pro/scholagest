package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface ITeacherService {
    // public Set<String> getTeacherTypes() throws Exception;

    public String createTeacher(String requestId, String teacherType, Map<String, Object> teacherProperties) throws Exception;

    public Set<String> getTeachers(String requestId) throws Exception;

    public Map<String, Map<String, Object>> getTeachersWithProperties(String requestId, Set<String> propertiesName) throws Exception;

    // Multiple year at once?
    // public Set<String> getTeacherClasses(String requestId, String teacherKey)
    // throws Exception;

    public void setTeacherProperties(String requestId, String teacherKey, Map<String, Object> properties) throws Exception;

    public Map<String, Object> getTeacherProperties(String requestId, String teacherKey, Set<String> propertiesName) throws Exception;
}
