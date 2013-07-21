package net.scholagest.managers.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.GradeObject;
import net.scholagest.objects.ObjectHelper;
import net.scholagest.objects.StudentObject;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class StudentManager extends ObjectManager implements IStudentManager {
    private final static String MEDICAL_INFO_SUFFIX = "#medicalInfo";
    private final static String PERSONAL_INFO_SUFFIX = "#personalInfo";
    private final static String YEARS_INFO_SUFFIX = "#yearsInfo";

    @Inject
    public StudentManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public StudentObject createStudent() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String studentUUID = UUID.randomUUID().toString();
        String studentKey = generateStudentKey(studentUUID);

        String studentBase = generateStudentBase(studentUUID);
        BaseObject personalInfo = new BaseObject(generatePersonalInfoKey(studentBase), CoreNamespace.tStudentPersonalInfo);
        BaseObject medicalInfo = new BaseObject(generateMedicalInfoKey(studentBase), CoreNamespace.tStudentMedicalInfo);

        StudentObject studentObject = new StudentObject(studentKey);

        studentObject.setPersonalInfoKey(personalInfo.getKey());
        studentObject.setMedicalInfoKey(medicalInfo.getKey());

        persistObject(transaction, personalInfo);
        persistObject(transaction, medicalInfo);
        persistObject(transaction, studentObject);

        transaction.insert(CoreNamespace.studentsBase, studentKey, studentKey, null);

        return studentObject;
    }

    @Override
    public void setPersonalProperties(String studentKey, Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        StudentObject student = new StudentObject(transaction, new ObjectHelper(getOntologyManager()), studentKey);
        BaseObject personalInfo = new BaseObject(transaction, new ObjectHelper(getOntologyManager()), student.getPersonalInfoKey());

        personalInfo.putAllProperties(properties);
    }

    @Override
    public void setMedicalProperties(String studentKey, Map<String, Object> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        StudentObject student = new StudentObject(transaction, new ObjectHelper(getOntologyManager()), studentKey);
        BaseObject personalInfo = new BaseObject(transaction, new ObjectHelper(getOntologyManager()), student.getMedicalInfoKey());

        personalInfo.putAllProperties(properties);
    }

    @Override
    public BaseObject getPersonalProperties(String studentKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        StudentObject student = new StudentObject(transaction, new ObjectHelper(getOntologyManager()), studentKey);
        return new BaseObject(transaction, new ObjectHelper(getOntologyManager()), student.getPersonalInfoKey());
    }

    @Override
    public BaseObject getMedicalProperties(String studentKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        StudentObject student = new StudentObject(transaction, new ObjectHelper(getOntologyManager()), studentKey);
        return new BaseObject(transaction, new ObjectHelper(getOntologyManager()), student.getMedicalInfoKey());
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
    public Set<StudentObject> getStudents() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        Set<StudentObject> students = new HashSet<>();

        for (String col : transaction.getColumns(CoreNamespace.studentsBase)) {
            String studentKey = (String) transaction.get(CoreNamespace.studentsBase, col, null);
            students.add(new StudentObject(transaction, new ObjectHelper(getOntologyManager()), studentKey));
        }

        return students;
    }

    @Override
    public StudentObject getStudentProperties(String studentKey, Set<String> properties) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new StudentObject(transaction, new ObjectHelper(getOntologyManager()), studentKey);
    }

    @Override
    public Map<String, BaseObject> getStudentGrades(String studentKey, Set<String> examKeys, String yearKey) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String studentYearsKey = (String) transaction.get(studentKey, CoreNamespace.pStudentYears, null);
        if (studentYearsKey == null) {
            studentYearsKey = createStudentYears(transaction, studentKey);
        }
        String studentYearNodeKey = (String) transaction.get(studentYearsKey, yearKey, null);
        if (studentYearNodeKey == null) {
            studentYearNodeKey = createStudentYearNode(transaction, studentYearsKey, yearKey);
        }

        Map<String, BaseObject> studentGrades = new HashMap<>();
        for (String examKey : examKeys) {
            String gradeKey = (String) transaction.get(studentYearNodeKey, examKey, null);

            if (gradeKey == null) {
                studentGrades.put(examKey, null);
            } else {
                GradeObject gradeObject = new GradeObject(transaction, new ObjectHelper(getOntologyManager()), gradeKey);
                studentGrades.put(examKey, gradeObject);
            }
        }

        return studentGrades;
    }

    private String createStudentYearNode(ITransaction transaction, String studentYearsKey, String yearKey) {
        String yearName = yearKey.substring(yearKey.indexOf("#") + 1);
        String studentYearNode = studentYearsKey + "_" + yearName;
        transaction.insert(studentYearsKey, yearKey, studentYearNode, null);
        return studentYearNode;
    }

    private String createStudentYears(ITransaction transaction, String studentKey) throws DatabaseException {
        String studentYearsKey = studentKey + YEARS_INFO_SUFFIX;
        transaction.insert(studentKey, CoreNamespace.pStudentYears, studentYearsKey, null);
        return studentYearsKey;
    }

    @Override
    public void persistStudentGrade(String studentKey, String yearKey, String examKey, BaseObject gradeObject) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String studentYearsKey = (String) transaction.get(studentKey, CoreNamespace.pStudentYears, null);

        String studentYearKey = (String) transaction.get(studentYearsKey, yearKey, null);

        persistObject(transaction, gradeObject);

        transaction.insert(studentYearKey, examKey, gradeObject.getKey(), null);
    }
}
