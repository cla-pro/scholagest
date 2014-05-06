package net.scholagest.old.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.old.objects.BaseObject;

public interface ITeacherService {
    public BaseObject createTeacher(String teacherType, Map<String, Object> teacherProperties) throws Exception;

    public Set<BaseObject> getTeachers() throws Exception;

    public Set<BaseObject> getTeachersWithProperties(Set<String> propertiesName) throws Exception;

    public void setTeacherProperties(String teacherKey, Map<String, Object> properties) throws Exception;

    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertiesName) throws Exception;
}
