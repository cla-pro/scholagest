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
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.objects.BranchObject;
import net.scholagest.objects.BranchType;
import net.scholagest.objects.ExamObject;
import net.scholagest.objects.PeriodObject;

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
        if (studentGrades.isEmpty()) {
            return;
        }

        BranchObject branchObject = branchManager.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchType)));

        for (String examKey : studentGrades.keySet()) {
            checkGradesType(branchObject, studentGrades);
            setGradeForExam(studentKey, yearKey, classKey, branchKey, periodKey, examKey, studentGrades.get(examKey));
        }

        PeriodObject periodObject = periodManager.getPeriodProperties(periodKey, new HashSet<String>());
        if (branchObject.getBranchType() == BranchType.NUMERICAL) {
            computePeriodAndBranchMeans(studentKey, periodObject, branchObject, yearKey);
        }
    }

    @Override
    public void setStudentPeriodMean(String studentKey, BaseObject studentMean, String yearKey, String classKey, String branchKey, String periodKey)
            throws Exception {
        if (studentMean == null) {
            return;
        }

        BranchObject branchObject = branchManager.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchType)));

        if (branchObject.getBranchType() == BranchType.NUMERICAL && studentMean != null) {
            throw new ScholagestException(ScholagestExceptionErrorCode.GENERAL, "Period mean cannot be saved in case of numerical branch");
        }

        PeriodObject periodObject = periodManager.getPeriodProperties(periodKey, new HashSet<String>());
        setGradeForExam(studentKey, yearKey, classKey, branchKey, periodObject.getKey(), periodObject.getMeanKey(), studentMean);
    }

    @Override
    public void setStudentBranchMean(String studentKey, BaseObject studentMean, String yearKey, String classKey, String branchKey) throws Exception {
        if (studentMean == null) {
            return;
        }

        BranchObject branchObject = branchManager.getBranchProperties(branchKey, new HashSet<String>(Arrays.asList(CoreNamespace.pBranchType)));

        if (branchObject.getBranchType() == BranchType.NUMERICAL && studentMean != null) {
            throw new ScholagestException(ScholagestExceptionErrorCode.GENERAL, "Branch mean cannot be saved in case of numerical branch");
        }

        setGradeForExam(studentKey, yearKey, classKey, branchKey, null, branchObject.getMeanKey(), studentMean);
    }

    private void computePeriodAndBranchMeans(String studentKey, PeriodObject periodObject, BranchObject branch, String yearKey) {
        String periodMeanKey = periodObject.getMeanKey();
        DBSet exams = periodObject.getExams();

        Map<String, BaseObject> gradeObjects = studentManager.getStudentGrades(studentKey, exams.values(), yearKey);
        Map<String, ExamObject> examObjects = getExamObjects(exams.values());

        double mean = computeMean(gradeObjects, examObjects);

        storeMean(studentKey, periodObject.getKey(), branch, yearKey, periodMeanKey, mean);

        Set<String> periodsMeanKeys = getPeriodsMeanKeys(branch);
        Map<String, BaseObject> periodMeanGradeObjects = studentManager.getStudentGrades(studentKey, periodsMeanKeys, yearKey);
        Map<String, ExamObject> periodMeanExamObjects = getExamObjects(periodsMeanKeys);

        double branchMean = computeMean(periodMeanGradeObjects, periodMeanExamObjects);

        storeMean(studentKey, null, branch, yearKey, branch.getMeanKey(), branchMean);
    }

    private Set<String> getPeriodsMeanKeys(BranchObject branch) {
        Set<String> periodsMeanKeys = new HashSet<>();

        for (String periodKey : branch.getPeriods().values()) {
            PeriodObject periodObject = periodManager.getPeriodProperties(periodKey, new HashSet<String>());
            periodsMeanKeys.add(periodObject.getMeanKey());
        }

        return periodsMeanKeys;
    }

    private void storeMean(String studentKey, String periodKey, BranchObject branch, String yearKey, String examKey, double mean) {
        BaseObject meanGrade = studentManager.getStudentGrades(studentKey, new HashSet<String>(Arrays.asList(examKey)), yearKey).get(examKey);
        if (meanGrade == null) {
            BaseObject meanGradeObject = new BaseObject(null, CoreNamespace.tGrade);
            meanGradeObject.putProperty(CoreNamespace.pGradeValue, "" + mean);
            setGradeForExam(studentKey, yearKey, branch.getClassKey(), branch.getKey(), periodKey, examKey, meanGradeObject);
        } else {
            meanGrade.putProperty(CoreNamespace.pGradeValue, "" + mean);
        }
    }

    private Map<String, ExamObject> getExamObjects(Set<String> exams) {
        Map<String, ExamObject> examObjects = new HashMap<>();
        for (String examKey : exams) {
            examObjects.put(examKey, examManager.getExamProperties(examKey, new HashSet<String>()));
        }
        return examObjects;
    }

    private double computeMean(Map<String, BaseObject> gradeObjects, Map<String, ExamObject> examObjects) {
        int coeffTotal = 0;
        double gradeTotal = 0.0;
        for (String examKey : examObjects.keySet()) {
            ExamObject examObject = examObjects.get(examKey);

            int coeff = examObject.getCoeff();
            Double grade = extractGrade(gradeObjects, examKey);
            if (grade != null) {
                coeffTotal += coeff;
                gradeTotal += coeff * grade;
            } else {
                System.out.println("Exam " + examKey + " has no grade");
            }
        }

        double mean = gradeTotal / coeffTotal;
        return mean;
    }

    private Double extractGrade(Map<String, BaseObject> gradeObjects, String examKey) {
        BaseObject grade = gradeObjects.get(examKey);
        if (grade == null) {
            return null;
        }
        String stringGrade = (String) grade.getProperty(CoreNamespace.pGradeValue);
        if (stringGrade == null) {
            return null;
        }
        return Double.parseDouble(stringGrade);
    }

    private void checkGradesType(BranchObject branch, Map<String, BaseObject> studentGrades) throws Exception {
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
                    throw new ScholagestException(ScholagestExceptionErrorCode.GRADE_NOT_NUMERICAL, "Invalid numerical grade for branch "
                            + branch.getKey() + ", exam: " + examKey + " and value: " + gradeValue);
                }
            }
        }
    }

    private void setGradeForExam(String studentKey, String yearKey, String classKey, String branchKey, String periodKey, String examKey,
            BaseObject grade) {
        BaseObject gradeObject = grade;
        if (grade.getKey() == null) {
            gradeObject = new BaseObject(generateGradeKey(yearKey, classKey, branchKey, periodKey, examKey), CoreNamespace.tGrade);
            gradeObject.putAllProperties(grade.getProperties());
            gradeObject.putProperty(CoreNamespace.pGradeExam, examKey);
        }

        studentManager.persistStudentGrade(studentKey, yearKey, examKey, gradeObject);
    }

    private String generateGradeKey(String yearKey, String classKey, String branchKey, String periodKey, String examKey) {
        // String yearName = getYearName(yearKey);
        // String className = getClassName(classKey);
        // String branchName = getBranchName(branchKey);
        // String periodName = getPeriodName(periodKey);
        // String examName = getExamName(examKey);

        return CoreNamespace.gradeNs + "#" + UUID.randomUUID().toString();
        // yearName + "/" + className + "/" + branchName + "/" + periodName +
        // "/" + examName + "#" + UUID.randomUUID().toString();
    }

    private String getYearName(String yearKey) {
        return (String) yearManager.getYearProperties(yearKey, new HashSet<String>()).getProperty(CoreNamespace.pYearName);
    }

    private String getClassName(String classKey) {
        return (String) classManager.getClassProperties(classKey, new HashSet<String>()).getProperty(CoreNamespace.pClassName);
    }

    private String getBranchName(String branchKey) {
        return (String) branchManager.getBranchProperties(branchKey, new HashSet<String>()).getProperty(CoreNamespace.pBranchName);
    }

    private String getPeriodName(String periodKey) {
        if (periodKey == null) {
            return "";
        }
        return (String) periodManager.getPeriodProperties(periodKey, new HashSet<String>()).getProperty(CoreNamespace.pPeriodName);
    }

    private String getExamName(String examKey) {
        return (String) examManager.getExamProperties(examKey, new HashSet<String>()).getProperty(CoreNamespace.pExamName);
    }
}
