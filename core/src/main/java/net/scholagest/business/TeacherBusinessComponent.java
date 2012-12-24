package net.scholagest.business;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.exception.ScholagestException;
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
        throw new ScholagestException(-1, -1, "Not yet implemented");
    }

    @Override
    public BaseObject createTeacher(String requestId, ITransaction transaction, String teacherType, Map<String, Object> teacherProperties)
            throws Exception {
        BaseObject teacherObject = teacherManager.createTeacher(requestId, transaction);

        if (teacherProperties != null) {
            teacherManager.setTeacherProperties(requestId, transaction, teacherObject.getKey(), teacherProperties);
        }

        return teacherObject;
    }

    @Override
    public Set<BaseObject> getTeachers(String requestId, ITransaction transaction) throws Exception {
        return teacherManager.getTeachers(requestId, transaction);
    }

    @Override
    public Set<BaseObject> getTeachersWithProperties(String requestId, ITransaction transaction, Set<String> propertiesName) throws Exception {
        Set<BaseObject> teacherSet = new HashSet<BaseObject>();

        for (BaseObject teacher : getTeachers(requestId, transaction)) {
            BaseObject info = teacherManager.getTeacherProperties(requestId, transaction, teacher.getKey(), propertiesName);
            teacherSet.add(info);
        }

        return teacherSet;
    }

    @Override
    public Set<BaseObject> getTeacherClasses(String requestId, ITransaction transaction, String teacherKey) throws Exception {
        throw new ScholagestException(-1, -1, "Not yet implemented");
    }

    @Override
    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> properties) throws Exception {
        teacherManager.setTeacherProperties(requestId, transaction, teacherKey, properties);
    }

    @Override
    public BaseObject getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception {
        return teacherManager.getTeacherProperties(requestId, transaction, teacherKey, propertiesName);
    }
}
