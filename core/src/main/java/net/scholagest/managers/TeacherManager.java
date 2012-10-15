package net.scholagest.managers;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;

import com.google.inject.Inject;

public class TeacherManager extends ObjectManager implements ITeacherManager {
    @Inject
    public TeacherManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public String createTeacher(String requestId, ITransaction transaction) throws Exception {
        String id = UUID.randomUUID().toString();
        String teacherKey = CoreNamespace.teacherNs + "#" + id;

        super.createObject(requestId, transaction, teacherKey, "tTeacher");

        String teacherBase = CoreNamespace.teacherNs + "/" + id;
        transaction.insert(CoreNamespace.teachersBase, teacherKey, teacherKey, null);

        // Classes node
        String classesKey = teacherBase + "#classes";
        transaction.insert(teacherKey, CoreNamespace.pTeacherClasses, classesKey, null);

        // Property nodes
        String propertiesKey = teacherBase + "#properties";
        transaction.insert(teacherKey, CoreNamespace.pTeacherProperties, propertiesKey, null);

        return teacherKey;
    }

    @Override
    public Set<String> getTeachers(String requestId, ITransaction transaction) throws Exception {
        return transaction.getColumns(CoreNamespace.teachersBase);
    }

    @Override
    public void setTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Map<String, Object> teacherProperties)
            throws Exception {
        super.setObjectProperties(requestId, transaction, teacherKey, teacherProperties);
    }

    @Override
    public Map<String, Object> getTeacherProperties(String requestId, ITransaction transaction, String teacherKey, Set<String> propertiesName)
            throws Exception {
        return super.getObjectProperties(requestId, transaction, teacherKey, propertiesName);
    }
}
