package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface ITeacherManager {
    public BaseObject createTeacher(String requestId, ITransaction transaction) throws Exception;

    public Set<BaseObject> getTeachers(String requestId, ITransaction transaction) throws Exception;

    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> teacherProperties)
            throws Exception;

    public BaseObject getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception;
}
