package net.scholagest.services;

import java.util.Map;
import java.util.Set;

public interface ITeacherService {
	public Set<String> getTeacherTypes() throws Exception;
	
    public String createTeacher(String teacherType,
            Map<String, Object> teacherProperties) throws Exception;
    
    public Set<String> getTeachers() throws Exception;
    
    public Map<String, Map<String, Object>> getTeachersWithProperties(
    		Set<String> propertiesName) throws Exception;

    //Multiple year at once?
    public Set<String> getTeacherClasses(String teacherKey) throws Exception;
    
    public void setTeacherInfo(String teacherKey,
            Map<String, Object> properties) throws Exception;
    
    public Map<String, Object> getTeacherInfo(String teacherKey,
            Set<String> propertiesName) throws Exception;
}
