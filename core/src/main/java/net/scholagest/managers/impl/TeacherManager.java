package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class TeacherManager extends ObjectManager implements ITeacherManager {
    @Inject
    public TeacherManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createTeacher(String requestId, ITransaction transaction) throws Exception {
        String id = UUID.randomUUID().toString();
        String teacherKey = CoreNamespace.teacherNs + "#" + id;

        BaseObject teacherObject = super.createObject(requestId, transaction, teacherKey, CoreNamespace.tTeacher);

        String teacherBase = CoreNamespace.teacherNs + "/" + id;
        transaction.insert(CoreNamespace.teachersBase, teacherKey, teacherKey, null);

        // Classes node
        String classesKey = teacherBase + "#classes";
        transaction.insert(teacherKey, CoreNamespace.pTeacherClasses, classesKey, null);

        // Property nodes
        String propertiesKey = teacherBase + "#properties";
        transaction.insert(teacherKey, CoreNamespace.pTeacherProperties, propertiesKey, null);

        return teacherObject;
    }

    @Override
    public Set<BaseObject> getTeachers(String requestId, ITransaction transaction) throws Exception {
        Set<BaseObject> teachers = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.teachersBase)) {
            String teacherKey = (String) transaction.get(CoreNamespace.teachersBase, col, null);
            teachers.add(new BaseObject(teacherKey, CoreNamespace.tTeacher));
        }

        return teachers;
    }

    @Override
    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> teacherProperties)
            throws Exception {
        super.setObjectProperties(requestId, transaction, teacherKey, teacherProperties);
    }

    @Override
    public BaseObject getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception {
        BaseObject teacherObject = new BaseObject(teacherKey, CoreNamespace.tTeacher);
        teacherObject.setProperties(super.getObjectProperties(requestId, transaction, teacherKey, propertiesName));

        return teacherObject;
    }
}
