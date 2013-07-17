package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.objects.BaseObject;

public interface ITeacherManager {
    public BaseObject createTeacher() throws Exception;

    public Set<BaseObject> getTeachers() throws Exception;

    public void setTeacherProperties(String teacherKey, Map<String, Object> teacherProperties) throws Exception;

    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertiesName) throws Exception;
}
