package net.scholagest.old.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.ITeacherManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.ObjectHelper;
import net.scholagest.old.objects.TeacherObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class TeacherManager extends ObjectManager implements ITeacherManager {
    @Inject
    public TeacherManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public TeacherObject createTeacher(Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String id = UUID.randomUUID().toString();
        String teacherKey = CoreNamespace.teacherNs + "#" + id;
        String teacherBase = CoreNamespace.teacherNs + "/" + id;

        TeacherObject teacherObject = new TeacherObject(teacherKey);

        teacherObject.putAllProperties(properties);

        String classesKey = teacherBase + "#classes";
        DBSet classesDBSet = new DBSet(transaction, classesKey);
        teacherObject.putProperty(CoreNamespace.pTeacherClasses, classesDBSet);

        persistObject(transaction, teacherObject);

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
