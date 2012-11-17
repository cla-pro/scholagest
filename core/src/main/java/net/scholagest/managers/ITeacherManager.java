package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface ITeacherManager {
    public String createTeacher(String requestId, ITransaction transaction) throws Exception;

    public Set<String> getTeachers(String requestId, ITransaction transaction) throws Exception;

    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> teacherProperties)
            throws Exception;

    public Map<String, Object> getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception;
}
