package net.scholagest.business.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.business.IStudentBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.managers.IBranchManager;
import net.scholagest.managers.IClassManager;
import net.scholagest.managers.IExamManager;
import net.scholagest.managers.IPeriodManager;
import net.scholagest.managers.IStudentManager;
import net.scholagest.managers.IYearManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BranchObject;
import net.scholagest.objects.BranchType;

import com.google.inject.Inject;

public class StudentBusinessComponent implements IStudentBusinessComponent {
    private IStudentManager studentManager;
    private IExamManager examManager;
    private IPeriodManager periodManager;
    private IBranchManager branchManager;
    private IClassManager classManager;
    private IYearManager yearManager;

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
    public BaseObject createStudent(Map<String, Object> personalProperties) throws Exception {
        BaseObject student = studentManager.createStudent();

        if (personalProperties != null) {
            studentManager.setPersonalProperties(student.getKey(), personalProperties);
        }

        return student;
    }

    @Override
    public void updateStudentProperties(String studentKey, Map<String, Object> personalProperties, Map<String, Object> medicalProperties)
            throws Exception {
        studentManager.setPersonalProperties(studentKey, personalProperties);
        studentManager.setMedicalProperties(studentKey, medicalProperties);
    }

    @Override
    public BaseObject getStudentPersonalProperties(String studentKey, Set<String> properties) throws Exception {
        return studentManager.getPersonalProperties(studentKey, properties);
    }

    @Override
    public BaseObject getStudentMedicalProperties(String studentKey, Set<String> properties) throws Exception {
        return studentManager.getMedicalProperties(studentKey, properties);
    }

    @Override
    public Set<BaseObject> getStudentsWithProperties(Set<String> properties) throws Exception {
        Set<BaseObject> students = new HashSet<>();

        for (BaseObject student : studentManager.getStudents()) {
            BaseObject personalInfo = studentManager.getPersonalProperties(student.getKey(), properties);
            student.setProperties(personalInfo.getProperties());
            students.add(student);
        }

        return students;
    }

    @Override
    public BaseObject getStudentProperties(String studentKey, Set<String> properties) throws Exception {
        return studentManager.getStudentProperties(studentKey, properties);
    }

    @Override
    public Map<String, Map<String, BaseObject>> getGrades(Set<String> studentKeys, Set<String> examKeys, String yearKey) throws Exception {
        Map<String, Map<String, BaseObject>> grades = new HashMap<>();

        for (String studentKey : studentKeys) {
            Map<String, BaseObject> studentGrades = studentManager.getStudentGrades(studentKey, examKeys, yearKey);
            grades.put(studentKey, studentGrades);
        }

        return grades;
    }

    @Override
    public void setStudentGrades(String studentKey, Map<String, BaseObject> studentGrades, String yearKey, String classKey, String branchKey,
            String periodKey) throws Exception {
        for (String examKey : studentGrades.keySet()) {
            checkGradesType(studentGrades, branchKey);
            setGradeForExam(studentKey, yearKey, classKey, branchKey, periodKey, examKey, studentGrades.get(examKey));
        }
    }

    private void checkGradesType(Map<String, BaseObject> studentGrades, String branchKey) throws Exception {
        BranchObject branch = branchManager.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchType)));
        boolean isNumerical = branch.getBranchType() == BranchType.NUMERICAL;

        if (isNumerical) {
            for (String examKey : studentGrades.keySet()) {
                BaseObject grade = studentGrades.get(examKey);
                String gradeValue = (String) grade.getProperty(CoreNamespace.pGradeValue);

                try {
                    if (gradeValue != null) {
                        Double.parseDouble(gradeValue);
                    }
                } catch (NumberFormatException e) {
                    throw new ScholagestException(ScholagestExceptionErrorCode.GRADE_NOT_NUMERICAL, "Invalid numerical grade for branch " + branchKey
                            + ", exam: " + examKey + " and value: " + gradeValue);
                }
            }
        }
    }

    private void setGradeForExam(String studentKey, String yearKey, String classKey, String branchKey, String periodKey, String examKey,
            BaseObject grade) throws Exception {
        BaseObject gradeObject = grade;
        if (grade.getKey() == null) {
            gradeObject = new BaseObject(generateGradeKey(yearKey, classKey, branchKey, periodKey, examKey), CoreNamespace.tGrade);
            gradeObject.setProperties(grade.getProperties());
        }

        studentManager.persistStudentGrade(studentKey, yearKey, examKey, gradeObject);
    }

    private String generateGradeKey(String yearKey, String classKey, String branchKey, String periodKey, String examKey) throws Exception {
        String yearName = getYearName(yearKey);
        String className = getClassName(classKey);
        String branchName = getBranchName(branchKey);
        String periodName = getPeriodName(periodKey);
        String examName = getExamName(examKey);

        return CoreNamespace.gradeNs + "/" + yearName + "/" + className + "/" + branchName + "/" + periodName + "/" + examName + "#"
                + UUID.randomUUID().toString();
    }

    private String getYearName(String yearKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pYearName);
        return (String) yearManager.getYearProperties(yearKey, properties).getProperty(CoreNamespace.pYearName);
    }

    private String getClassName(String classKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pClassName);
        return (String) classManager.getClassProperties(classKey, properties).getProperty(CoreNamespace.pClassName);
    }

    private String getBranchName(String branchKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pBranchName);
        return (String) branchManager.getBranchProperties(branchKey, properties).getProperty(CoreNamespace.pBranchName);
    }

    private String getPeriodName(String periodKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pPeriodName);
        return (String) periodManager.getPeriodProperties(periodKey, properties).getProperty(CoreNamespace.pPeriodName);
    }

    private String getExamName(String examKey) throws Exception {
        Set<String> properties = new HashSet<>();
        properties.add(CoreNamespace.pExamName);
        return (String) examManager.getExamProperties(examKey, properties).getProperty(CoreNamespace.pExamName);
    }
}
