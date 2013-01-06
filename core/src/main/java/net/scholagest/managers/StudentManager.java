package net.scholagest.managers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class StudentManager extends ObjectManager implements IStudentManager {
    private final static String MEDICAL_INFO_SUFFIX = "#medicalInfo";
    private final static String PERSONAL_INFO_SUFFIX = "#personalInfo";

    @Inject
    public StudentManager(OntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public BaseObject createStudent(String requestId, ITransaction transaction) throws Exception {
        String studentUUID = UUID.randomUUID().toString();
        String studentKey = generateStudentKey(studentUUID);
        BaseObject studentObject = super.createObject(requestId, transaction, studentKey, CoreNamespace.tStudent);

        transaction.insert(CoreNamespace.studentsBase, studentKey, studentKey, null);

        String studentBase = generateStudentBase(studentUUID);
        BaseObject personalInfoKey = super.createObject(requestId, transaction, generatePersonalInfoKey(studentBase),
                CoreNamespace.tStudentPersonalInfo);
        BaseObject medicalInfoKey = super
                .createObject(requestId, transaction, generateMedicalInfoKey(studentBase), CoreNamespace.tStudentMedicalInfo);

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pStudentPersonalInfo, personalInfoKey.getKey());
        properties.put(CoreNamespace.pStudentMedicalInfo, medicalInfoKey.getKey());
        super.setObjectProperties(requestId, transaction, studentKey, properties);

        return studentObject;
    }

    @Override
    public void setPersonalProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> properties) throws Exception {
        String personalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentPersonalInfo, null);
        super.setObjectProperties(requestId, transaction, personalInfoKey, properties);
    }

    @Override
    public void setMedicalProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> properties) throws Exception {
        String medicalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentMedicalInfo, null);
        super.setObjectProperties(requestId, transaction, medicalInfoKey, properties);
    }

    @Override
    public BaseObject getPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception {
        String personalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentPersonalInfo, null);

        BaseObject personalInfo = new BaseObject(personalInfoKey, CoreNamespace.tStudentPersonalInfo);
        personalInfo.setProperties(super.getObjectProperties(requestId, transaction, personalInfoKey, properties));

        return personalInfo;
    }

    @Override
    public BaseObject getMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception {
        String medicalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentMedicalInfo, null);

        BaseObject medicalInfo = new BaseObject(medicalInfoKey, CoreNamespace.tStudentMedicalInfo);
        medicalInfo.setProperties(super.getObjectProperties(requestId, transaction, medicalInfoKey, properties));

        return medicalInfo;
    }

    private String generateStudentKey(String studentUUID) {
        return CoreNamespace.studentNs + "#" + studentUUID;
    }

    private String generateStudentBase(String studentUUID) {
        return CoreNamespace.studentNs + "/" + studentUUID;
    }

    private String generatePersonalInfoKey(String studentBase) {
        return studentBase + PERSONAL_INFO_SUFFIX;
    }

    private String generateMedicalInfoKey(String studentBase) {
        return studentBase + MEDICAL_INFO_SUFFIX;
    }

    @Override
    public Set<BaseObject> getStudents(String requestId, ITransaction transaction) throws Exception {
        Set<BaseObject> students = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.studentsBase)) {
            String studentKey = (String) transaction.get(CoreNamespace.studentsBase, col, null);
            students.add(new BaseObject(studentKey, CoreNamespace.tStudent));
        }

        return students;
    }

    @Override
    public BaseObject getStudentProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception {
        BaseObject studentObject = new BaseObject(studentKey, CoreNamespace.tStudent);
        studentObject.setProperties(super.getObjectProperties(requestId, transaction, studentKey, properties));

        return studentObject;
    }
}
