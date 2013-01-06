package net.scholagest.business;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.objects.BaseObject;

public interface ITeacherBusinessComponent {
    public Set<String> getTeacherTypes() throws Exception;

    public BaseObject createTeacher(String requestId, ITransaction transaction, String teacherType, Map<String, Object> teacherProperties)
            throws Exception;

    public Set<BaseObject> getTeachers(String requestId, ITransaction transaction) throws Exception;

    public Set<BaseObject> getTeachersWithProperties(String requestId, ITransaction transaction, Set<String> propertiesName) throws Exception;

    public Set<BaseObject> getTeacherClasses(String requestId, ITransaction transaction, String teacherKey) throws Exception;

    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> properties) throws Exception;

    public BaseObject getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception;
}
