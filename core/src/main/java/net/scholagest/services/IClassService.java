package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface IClassService {
    //Create a new class into the current year. Throws an exception if no year
    //is currently running or if a class with the same name already exists.
    public String createClass(String className,
    		String classType) throws Exception;
    
    public void removeClass(String classKey) throws Exception;
    
    public void assignTeacherToClass(String classKey, String teacherKey)
            throws Exception;
    
    public void unassignTeacherToClass(String classKey, String teacherKey)
            throws Exception;
    
    public void assignStudentToClass(String classKey, String studentKey)
            throws Exception;
    
    public void unassignStudentToClass(String classKey, String studentKey)
            throws Exception;
    
    public void addBranch(String classKey, String branchKey) throws Exception;
    
    public void setClassInfo(String classKey,
    		Map<String, Object> properties) throws Exception;
    
    public Map<String, Object> getClassInfo(String classKey,
    		Set<String> propertiesName) throws Exception;
}
