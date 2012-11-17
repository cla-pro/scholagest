package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IClassService {
    // Create a new class into the current year. Throws an exception if no year
    // is currently running or if a class with the same name already exists.
    public String createClass(String requestId, Map<String, Object> classProperties) throws Exception;

    public Map<String, Set<String>> getClasses(String requestId, Set<String> yearKeyList) throws Exception;

    public Map<String, Object> getClassProperties(String requestId, String classKey, Set<String> propertiesName) throws Exception;

    // public void removeClass(String classKey) throws Exception;
    //
    // public void assignTeacherToClass(String classKey, String teacherKey)
    // throws Exception;
    //
    // public void unassignTeacherToClass(String classKey, String teacherKey)
    // throws Exception;
    //
    // public void assignStudentToClass(String classKey, String studentKey)
    // throws Exception;
    //
    // public void unassignStudentToClass(String classKey, String studentKey)
    // throws Exception;
    //
    // public void addBranch(String classKey, String branchKey) throws
    // Exception;
    //
    // public void setClassInfo(String classKey, Map<String, Object> properties)
    // throws Exception;
}
