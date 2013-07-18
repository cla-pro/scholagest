package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.ObjectHelper;
import net.scholagest.objects.TeacherObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class TeacherManager extends ObjectManager implements ITeacherManager {
    @Inject
    public TeacherManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public TeacherObject createTeacher() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String id = UUID.randomUUID().toString();
        String teacherKey = CoreNamespace.teacherNs + "#" + id;
        String teacherBase = CoreNamespace.teacherNs + "/" + id;

        TeacherObject teacherObject = new TeacherObject(teacherKey);

        // Classes node
        String classesKey = teacherBase + "#classes";
        teacherObject.putProperty(CoreNamespace.pTeacherClasses, classesKey);

        // Property nodes
        String propertiesKey = teacherBase + "#properties";
        teacherObject.putProperty(CoreNamespace.pTeacherProperties, propertiesKey);

        transaction.insert(CoreNamespace.teachersBase, teacherKey, teacherKey, null);

        return teacherObject;
    }

    @Override
    public Set<TeacherObject> getTeachers() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        Set<TeacherObject> teachers = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.teachersBase)) {
            String teacherKey = (String) transaction.get(CoreNamespace.teachersBase, col, null);
            teachers.add(new TeacherObject(transaction, new ObjectHelper(getOntologyManager()), teacherKey));
        }

        return teachers;
    }

    @Override
    public void setTeacherProperties(String teacherKey, Map<String, Object> teacherProperties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        TeacherObject teacherObject = new TeacherObject(transaction, new ObjectHelper(getOntologyManager()), teacherKey);
        teacherObject.putAllProperties(teacherProperties);
    }

    @Override
    public TeacherObject getTeacherProperties(String teacherKey, Set<String> propertiesName) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new TeacherObject(transaction, new ObjectHelper(getOntologyManager()), teacherKey);
    }
}
