package net.scholagest.business;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.ITeacherManager;

import com.google.inject.Inject;

public class TeacherBusinessComponent implements ITeacherBusinessComponent {
    private ITeacherManager teacherManager;

    @Inject
    public TeacherBusinessComponent(ITeacherManager teacherManager) {
        this.teacherManager = teacherManager;
    }

    @Override
    public Set<String> getTeacherTypes() throws Exception {
        throw new ScholagestException(-1, -1, "Not yet implemented");
    }

    @Override
    public String createTeacher(String requestId, ITransaction transaction, String teacherType, Map<String, Object> teacherProperties)
            throws Exception {
        String teacherKey = teacherManager.createTeacher(requestId, transaction);

        if (teacherProperties != null) {
            teacherManager.setTeacherProperties(requestId, transaction, teacherKey, teacherProperties);
        }

        return teacherKey;
    }

    @Override
    public Set<String> getTeachers(String requestId, ITransaction transaction) throws Exception {
        return teacherManager.getTeachers(requestId, transaction);
    }

    @Override
    public Map<String, Map<String, Object>> getTeachersWithProperties(String requestId, ITransaction transaction, Set<String> propertiesName)
            throws Exception {
        Map<String, Map<String, Object>> teachers = new HashMap<String, Map<String, Object>>();

        for (String teacherKey : getTeachers(requestId, transaction)) {
            Map<String, Object> info = teacherManager.getTeacherProperties(requestId, transaction, teacherKey, propertiesName);
            teachers.put(teacherKey, info);
        }

        return teachers;
    }

    @Override
    public Set<String> getTeacherClasses(String requestId, ITransaction transaction, String teacherKey) throws Exception {
        throw new ScholagestException(-1, -1, "Not yet implemented");
    }

    @Override
    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> properties) throws Exception {
        teacherManager.setTeacherProperties(requestId, transaction, teacherKey, properties);
    }

    @Override
    public Map<String, Object> getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception {
        return teacherManager.getTeacherProperties(requestId, transaction, teacherKey, propertiesName);
    }
}
