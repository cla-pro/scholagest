package net.scholagest.managers.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.objects.BaseObject;

import com.google.inject.Inject;

public class StudentManager extends ObjectManager implements IStudentManager {
    private final static String MEDICAL_INFO_SUFFIX = "#medicalInfo";
    private final static String PERSONAL_INFO_SUFFIX = "#personalInfo";
    private final static String YEARS_INFO_SUFFIX = "#yearsInfo";

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
        BaseObject personalInfo = super
                .createObject(requestId, transaction, generatePersonalInfoKey(studentBase), CoreNamespace.tStudentPersonalInfo);
        BaseObject medicalInfo = super.createObject(requestId, transaction, generateMedicalInfoKey(studentBase), CoreNamespace.tStudentMedicalInfo);

        Map<String, Object> properties = new HashMap<>();
        properties.put(CoreNamespace.pStudentPersonalInfo, personalInfo.getKey());
        properties.put(CoreNamespace.pStudentMedicalInfo, medicalInfo.getKey());
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

    @Override
    public Map<String, BaseObject> getStudentGrades(String requestId, ITransaction transaction, String studentKey, Set<String> examKeys,
            String yearKey) throws Exception {
        String studentYearsKey = (String) transaction.get(studentKey, CoreNamespace.pStudentYears, null);
        if (studentYearsKey == null) {
            studentYearsKey = createStudentYears(requestId, transaction, studentKey);
        }
        String studentYearNodeKey = (String) transaction.get(studentYearsKey, yearKey, null);
        if (studentYearNodeKey == null) {
            studentYearNodeKey = createStudentYearNode(requestId, transaction, studentYearsKey, yearKey);
        }

        Map<String, BaseObject> studentGrades = new HashMap<>();
        for (String examKey : examKeys) {
            String gradeKey = (String) transaction.get(studentYearNodeKey, examKey, null);

            if (gradeKey == null) {
                studentGrades.put(examKey, null);
            } else {
                BaseObject examObject = new BaseObject(gradeKey, CoreNamespace.tGrade);
                Map<String, Object> examProperties = new HashMap<>();
                examProperties.put(CoreNamespace.pGradeValue, transaction.get(gradeKey, CoreNamespace.pGradeValue, null));
                examObject.setProperties(examProperties);
                studentGrades.put(examKey, examObject);
            }
        }

        return studentGrades;
    }

    private String createStudentYearNode(String requestId, ITransaction transaction, String studentYearsKey, String yearKey) throws DatabaseException {
        String yearName = yearKey.substring(yearKey.indexOf("#") + 1);
        String studentYearNode = studentYearsKey + "_" + yearName;
        transaction.insert(studentYearsKey, yearKey, studentYearNode, null);
        return studentYearNode;
    }

    private String createStudentYears(String requestId, ITransaction transaction, String studentKey) throws DatabaseException {
        String studentYearsKey = studentKey + YEARS_INFO_SUFFIX;
        transaction.insert(studentKey, CoreNamespace.pStudentYears, studentYearsKey, null);
        return studentYearsKey;
    }

    @Override
    public void persistStudentGrade(String requestId, ITransaction transaction, String studentKey, String yearKey, String examKey,
            BaseObject gradeObject) throws Exception {
        String studentYearsKey = (String) transaction.get(studentKey, CoreNamespace.pStudentYears, null);

        String studentYearKey = (String) transaction.get(studentYearsKey, yearKey, null);

        String gradeKey = gradeObject.getKey();
        transaction.insert(gradeKey, RDF.type, gradeObject.getType(), null);
        super.setObjectProperties(requestId, transaction, gradeKey, gradeObject.getProperties());

        transaction.insert(studentYearKey, examKey, gradeKey, null);
    }
}
