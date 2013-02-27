package net.scholagest.business;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.kdom.DBToKdomConverter;

import com.google.inject.Inject;

public class StudentBusinessComponent implements IStudentBusinessComponent {
    private IStudentManager studentManager;
    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

    private DBToKdomConverter converter = new DBToKdomConverter();

    @Inject
    public StudentBusinessComponent(IStudentManager studentManager, IYearManager yearManager, IClassManager classManager,
            IBranchManager branchManager, IPeriodManager periodManager, IExamManager examManager) {
        this.studentManager = studentManager;
        this.yearManager = yearManager;
        this.classManager = classManager;
        this.branchManager = branchManager;
        this.periodManager = periodManager;
        this.examManager = examManager;
    }

    @Override
    public BaseObject createStudent(String requestId, ITransaction transaction, Map<String, Object> personalProperties) throws Exception {
        BaseObject student = studentManager.createStudent(requestId, transaction);

        if (personalProperties != null) {
            studentManager.setPersonalProperties(requestId, transaction, student.getKey(), personalProperties);
        }

        return converter.convertDbToKdom(student);
    }

    @Override
    public void updateStudentProperties(String requestId, ITransaction transaction, String studentKey, Map<String, Object> personalProperties,
            Map<String, Object> medicalProperties) throws Exception {
        studentManager.setPersonalProperties(requestId, transaction, studentKey, personalProperties);
        studentManager.setMedicalProperties(requestId, transaction, studentKey, medicalProperties);
    }

    @Override
    public BaseObject getStudentPersonalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception {
        BaseObject personalProperties = studentManager.getPersonalProperties(requestId, transaction, studentKey, properties);
        return converter.convertDbToKdom(personalProperties);
    }

    @Override
    public BaseObject getStudentMedicalProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties)
            throws Exception {
        BaseObject medicalProperties = studentManager.getMedicalProperties(requestId, transaction, studentKey, properties);
        return converter.convertDbToKdom(medicalProperties);
    }

    @Override
    public Set<BaseObject> getStudentsWithProperties(String requestId, ITransaction transaction, Set<String> properties) throws Exception {
        Set<BaseObject> students = new HashSet<>();

        for (BaseObject student : studentManager.getStudents(requestId, transaction)) {
            BaseObject personalInfo = studentManager.getPersonalProperties(requestId, transaction, student.getKey(), properties);
            student.setProperties(personalInfo.getProperties());
            students.add(converter.convertDbToKdom(student));
        }

        return students;
    }

    @Override
    public BaseObject getStudentProperties(String requestId, ITransaction transaction, String studentKey, Set<String> properties) throws Exception {
        BaseObject studentObject = studentManager.getStudentProperties(requestId, transaction, studentKey, properties);
        return converter.convertDbToKdom(studentObject);
    }

    @Override
    public Map<String, Map<String, BaseObject>> getGrades(String requestId, ITransaction transaction, Set<String> studentKeys, Set<String> examKeys,
            String yearKey) throws Exception {
        Map<String, Map<String, BaseObject>> grades = new HashMap<>();

        for (String studentKey : studentKeys) {
            Map<String, BaseObject> studentGrades = studentManager.getStudentGrades(requestId, transaction, studentKey, examKeys, yearKey);
            grades.put(studentKey, studentGrades);
        }

        return grades;
    }

    @Override
    public void setStudentGrades(String requestId, ITransaction transaction, String studentKey, Map<String, BaseObject> studentGrades,
            String yearKey, String classKey, String branchKey, String periodKey) throws Exception {
        for (String examKey : studentGrades.keySet()) {
            setGradeForExam(requestId, transaction, studentKey, yearKey, classKey, branchKey, periodKey, examKey, studentGrades.get(examKey));
        }
    }

    private void setGradeForExam(String requestId, ITransaction transaction, String studentKey, String yearKey, String classKey, String branchKey,
            String periodKey, String examKey, BaseObject grade) throws Exception {
        BaseObject gradeObject = grade;
        if (grade.getKey() == null) {
            gradeObject = new BaseObject(generateGradeKey(requestId, transaction, yearKey, classKey, branchKey, periodKey, examKey),
                    CoreNamespace.tGrade);
            gradeObject.setProperties(grade.getProperties());
        }

        studentManager.persistStudentGrade(requestId, transaction, studentKey, yearKey, examKey, gradeObject);
    }

    private String generateGradeKey(String requestId, ITransaction transaction, String yearKey, String classKey, String branchKey, String periodKey,
            String examKey) throws Exception {
        String yearName = getYearName(requestId, transaction, yearKey);
        String className = getClassName(requestId, transaction, classKey);
        String branchName = getBranchName(requestId, transaction, branchKey);
        String periodName = getPeriodName(requestId, transaction, periodKey);
        String examName = getExamName(requestId, transaction, examKey);

        return CoreNamespace.gradeNs + "/" + yearName + "/" + className + "/" + branchName + "/" + periodName + "/" + examName + "#"
                + UUID.randomUUID().toString();
    }

    private String getYearName(String requestId, ITransaction transaction, String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        return (String) yearManager.getYearProperties(requestId, transaction, yearKey, properties).getProperty(CoreNamespace.pYearName);
    }

    private String getClassName(String requestId, ITransaction transaction, String classKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pClassName);
        return (String) classManager.getClassProperties(requestId, transaction, classKey, properties).getProperty(CoreNamespace.pClassName);
    }

    private String getBranchName(String requestId, ITransaction transaction, String branchKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pBranchName);
        return (String) branchManager.getBranchProperties(requestId, transaction, branchKey, properties).getProperty(CoreNamespace.pBranchName);
    }

    private String getPeriodName(String requestId, ITransaction transaction, String periodKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pPeriodName);
        return (String) periodManager.getPeriodProperties(requestId, transaction, periodKey, properties).getProperty(CoreNamespace.pPeriodName);
    }

    private String getExamName(String requestId, ITransaction transaction, String examKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pExamName);
        return (String) examManager.getExamProperties(requestId, transaction, examKey, properties).getProperty(CoreNamespace.pExamName);
    }
}
