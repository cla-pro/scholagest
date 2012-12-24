package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface IClassService {
    // Create a new class into the current year. Throws an exception if no year
    // is currently running or if a class with the same name already exists.
    public BaseObject createClass(String requestId, Map<String, Object> classProperties) throws Exception;

    public Map<String, Set<BaseObject>> getClassesForYears(String requestId, Set<String> yearKeyList) throws Exception;

    public BaseObject getClassProperties(String requestId, String classKey, Set<String> propertiesName) throws Exception;

    public void setClassProperties(String requestId, String classKey, Map<String, Object> properties) throws Exception;

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
