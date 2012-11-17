package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface ITeacherBusinessComponent {
    public Set<String> getTeacherTypes() throws Exception;

    public String createTeacher(String requestId, ITransaction transaction, String teacherType, Map<String, Object> teacherProperties)
            throws Exception;

    public Set<String> getTeachers(String requestId, ITransaction transaction) throws Exception;

    public Map<String, Map<String, Object>> getTeachersWithProperties(String requestId, ITransaction transaction, Set<String> propertiesName)
            throws Exception;

    public Set<String> getTeacherClasses(String requestId, ITransaction transaction, String teacherKey) throws Exception;

    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> properties) throws Exception;

    public Map<String, Object> getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception;
}
