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
import net.scholagest.managers.ontology.types.DBMap;
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

        DBMap studentYearMap = getStudentYearMap(studentKey, transaction);
        DBMap studentGradeMap = getStudentGradeMap(studentKey, yearKey, transaction, studentYearMap);

        return storeAndReturnGrades(examKeys, transaction, studentGradeMap);
    }

    private Map<String, BaseObject> storeAndReturnGrades(Set<String> examKeys, ITransaction transaction, DBMap studentGradeMap) {
        Map<String, BaseObject> studentGrades = new HashMap<>();
        for (String examKey : examKeys) {
            String gradeKey = studentGradeMap.get(examKey);

            if (gradeKey == null) {
                studentGrades.put(examKey, null);
            } else {
                GradeObject gradeObject = new GradeObject(transaction, new ObjectHelper(getOntologyManager()), gradeKey);
                studentGrades.put(examKey, gradeObject);
            }
        }
        return studentGrades;
    }

    private DBMap getStudentYearMap(String studentKey, ITransaction transaction) {
        String studentYearsKey = (String) transaction.get(studentKey, CoreNamespace.pStudentScholarInfo, null);
        DBMap studentYearsMap = null;
        if (studentYearsKey == null) {
            studentYearsMap = createStudentYears(transaction, studentKey);
            transaction.insert(studentKey, CoreNamespace.pStudentScholarInfo, studentYearsMap.getKey(), null);
        } else {
            studentYearsMap = new DBMap(transaction, studentYearsKey);
        }
        return studentYearsMap;
    }

    private DBMap getStudentGradeMap(String studentKey, String yearKey, ITransaction transaction, DBMap studentYearsMap) {
        String studentGradeMapKey = studentYearsMap.get(yearKey);
        DBMap studentGradeMap = null;
        if (studentGradeMapKey == null) {
            studentGradeMap = createStudentYearNode(transaction, studentKey, yearKey);
            studentYearsMap.put(yearKey, studentGradeMap.getKey());
        } else {
            studentGradeMap = new DBMap(transaction, studentGradeMapKey);
        }
        return studentGradeMap;
    }

    private DBMap createStudentYearNode(ITransaction transaction, String studentKey, String yearKey) {
        String yearUUID = yearKey.substring(yearKey.indexOf("#") + 1);
        String studentGradeMapKey = studentKey + "_grades_" + yearUUID;
        DBMap studentGradeMap = DBMap.createDBMap(transaction, studentGradeMapKey);
        return studentGradeMap;
    }

    private DBMap createStudentYears(ITransaction transaction, String studentKey) throws DatabaseException {
        String studentYearsKey = studentKey + YEARS_INFO_SUFFIX;
        DBMap studentYearsMap = DBMap.createDBMap(transaction, studentYearsKey);
        return studentYearsMap;
    }

    @Override
    public void persistStudentGrade(String studentKey, String yearKey, String examKey, BaseObject gradeObject) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        DBMap studentYearMap = getStudentYearMap(studentKey, transaction);
        DBMap studentGradeMap = getStudentGradeMap(studentKey, yearKey, transaction, studentYearMap);

        persistObject(transaction, gradeObject);

        studentGradeMap.put(examKey, gradeObject.getKey());
    }
}
