package net.scholagest.managers.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ITeacherManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class TeacherManager extends ObjectManager implements ITeacherManager {
    @Inject
    public TeacherManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createTeacher() throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String id = UUID.randomUUID().toString();
        String teacherKey = CoreNamespace.teacherNs + "#" + id;

        BaseObject teacherObject = createObject(transaction, teacherKey, CoreNamespace.tTeacher);

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
    public Set<BaseObject> getTeachers() throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        Set<BaseObject> teachers = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.teachersBase)) {
            String teacherKey = (String) transaction.get(CoreNamespace.teachersBase, col, null);
            teachers.add(new BaseObject(teacherKey, CoreNamespace.tTeacher));
        }

        return teachers;
    }

    @Override
    public void setTeacherProperties(String teacherKey, Map<String, Object> teacherProperties) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        setObjectProperties(transaction, teacherKey, teacherProperties);
    }

    @Override
    public BaseObject getTeacherProperties(String teacherKey, Set<String> propertiesName) throws Exception {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        BaseObject teacherObject = new BaseObject(teacherKey, CoreNamespace.tTeacher);
        teacherObject.setProperties(getObjectProperties(transaction, teacherKey, propertiesName));

        return teacherObject;
    }
}
