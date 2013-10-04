package net.scholagest.business.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.business.ITeacherBusinessComponent;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.objects.TeacherObject;

import com.google.inject.Inject;

public class TeacherBusinessComponent implements ITeacherBusinessComponent {
    private ITeacherManager teacherManager;

    @Inject
    public TeacherBusinessComponent(ITeacherManager teacherManager) {
        this.teacherManager = teacherManager;
    }

    @Override
    public Set<String> getTeacherTypes() {
        throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.GENERAL, "Not yet implemented");
    }

    @Override
    public TeacherObject createTeacher(String teacherType, Map<String, Object> teacherProperties) {
        teacherProperties.put("pTeacherType", teacherType);
        TeacherObject teacherObject = teacherManager.createTeacher(teacherProperties);

        return teacherObject;
    }

    @Override
    public Set<TeacherObject> getTeachers() {
        return teacherManager.getTeachers();
    }

    @Override
    public Set<TeacherObject> getTeachersWithProperties(Set<String> propertiesName) {
        Set<TeacherObject> teacherSet = new HashSet<>();

        for (TeacherObject teacher : getTeachers()) {
            TeacherObject info = teacherManager.getTeacherProperties(teacher.getKey(), propertiesName);
            teacherSet.add(info);
        }

        return teacherSet;
    }

    @Override
    public Set<TeacherObject> getTeacherClasses(String teacherKey) {
        throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.GENERAL, "Not yet implemented");
    }

    @Override
    public void setTeacherProperties(String teacherKey, Map<String, Object> properties) {
        teacherManager.setTeacherProperties(teacherKey, properties);
    }

    @Override
    public TeacherObject getTeacherProperties(String teacherKey, Set<String> propertiesName) {
        return teacherManager.getTeacherProperties(teacherKey, propertiesName);
    }
}
