package net.scholagest.business.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class TeacherBusinessComponent implements ITeacherBusinessComponent {
    private ITeacherManager teacherManager;

    @Inject
    public TeacherBusinessComponent(ITeacherManager teacherManager) {
        this.teacherManager = teacherManager;
    }

    @Override
    public Set<String> getTeacherTypes() throws Exception {
        throw new ScholagestException(ScholagestExceptionErrorCode.GENERAL, "Not yet implemented");
    }

    @Override
    public BaseObject createTeacher(String teacherType, Map<String, Object> teacherProperties) throws Exception {
        BaseObject teacherObject = teacherManager.createTeacher();

        if (teacherProperties != null) {
            teacherManager.setTeacherProperties(teacherObject.getKey(), teacherProperties);
        }

        return teacherObject;
    }

    @Override
    public Set<BaseObject> getTeachers() throws Exception {
        return teacherManager.getTeachers();
    }

    @Override
    public Set<BaseObject> getTeachersWithProperties(Set<String> propertiesName) throws Exception {
        Set<BaseObject> teacherSet = new HashSet<BaseObject>();

        for (BaseObject teacher : getTeachers()) {
            BaseObject info = teacherManager.getTeacherProperties(teacher.getKey(), propertiesName);
            teacherSet.add(info);
        }

        return teacherSet;
    }

    @Override
    public Set<BaseObject> getTeacherClasses(String teacherKey) throws Exception {
        throw new ScholagestException(ScholagestExceptionErrorCode.GENERAL, "Not yet implemented");
    }

    @Override
    public void setTeacherProperties(String teacherKey, Map<String, Object> properties) throws Exception {
        teacherManager.setTeacherProperties(teacherKey, properties);
    }

    @Override
    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertiesName) throws Exception {
        return teacherManager.getTeacherProperties(teacherKey, propertiesName);
    }
}
