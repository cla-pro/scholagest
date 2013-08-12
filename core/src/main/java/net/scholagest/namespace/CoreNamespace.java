package net.scholagest.namespace;

public class CoreNamespace {
    public static final String scholagestNs = "http://scholagest.net";

    // Ontology
    public static final String ontologyNs = scholagestNs + "/ontology";
    public static final String ontologyBase = ontologyNs + "#ontology";
    public static final String pOntologyClassDomain = "pOntologyClassDomain";
    public static final String pOntologyClassRange = "pOntologyClassRange";

    // Types
    public static final String tGroup = scholagestNs + "#tGroup";
    public static final String tGrade = scholagestNs + "#tGrade";
    public static final String tExam = scholagestNs + "#tExam";
    public static final String tBranch = scholagestNs + "#tBranch";
    public static final String tPeriod = scholagestNs + "#tPeriod";
    public static final String tClass = scholagestNs + "#tClass";
    public static final String tTeacher = scholagestNs + "#tTeacher";
    public static final String tUser = scholagestNs + "#tUser";
    public static final String tStudent = scholagestNs + "#tStudent";
    public static final String tStudentPersonalInfo = scholagestNs + "#tStudentPersonalInfo";
    public static final String tStudentMedicalInfo = scholagestNs + "#tStudentMedicalInfo";
    public static final String tStudentYears = scholagestNs + "#tStudentYears";
    public static final String tYear = scholagestNs + "#tYear";
    public static final String tToken = scholagestNs + "#tToken";
    public static final String tPage = scholagestNs + "#tPage";

    // Teachers
    public static final String teacherNs = scholagestNs + "/teacher";

    private static final String pTeacherNs = "pTeacher";
    public static final String pTeacherClasses = pTeacherNs + "Classes";
    public static final String pTeacherProperties = pTeacherNs + "Properties";
    public static final String pTeacherUser = pTeacherNs + "User";

    private static final String pUserNs = "pUser";
    public static final String pUserUsername = pUserNs + "Username";
    public static final String pUserPassword = pUserNs + "Password";
    public static final String pUserTeacher = pUserNs + "Teacher";

    private static final String pTokenNs = "pToken";
    public static final String pTokenEndValidityTime = pTokenNs + "EndValidityTime";
    public static final String pTokenUser = pTokenNs + "User";

    private static final String pPageNs = "pPage";
    public static final String pPageRoles = pPageNs + "Roles";
    public static final String pPagePath = pPageNs + "Path";

    // Students
    public static final String studentNs = scholagestNs + "/student";

    private static final String pStudentNs = "pStudent";
    public static final String pStudentPersonalInfo = pStudentNs + "PersonalInfo";
    public static final String pStudentMedicalInfo = pStudentNs + "MedicalInfo";
    public static final String pStudentYears = pStudentNs + "Years";

    public static final String yearNs = scholagestNs + "/year";
    public static final String pYearCurrent = "pYearCurrent";
    public static final String pYearName = "pYearName";
    public static final String pYearClasses = "pYearClasses";

    public static final String classNs = scholagestNs + "/class";
    public static final String pClassName = "pClassName";
    public static final String pClassYear = "pClassYear";
    public static final String pClassStudents = "pClassStudents";
    public static final String pClassTeachers = "pClassTeachers";
    public static final String pClassBranches = "pClassBranches";

    public static final String branchNs = scholagestNs + "/branch";
    public static final String pBranchName = "pBranchName";
    public static final String pBranchClass = "pBranchClass";
    public static final String pBranchPeriods = "pBranchPeriods";
    public static final String pBranchType = "pBranchType";
    public static final String pBranchMean = "pBranchMean";

    public static final String periodNs = scholagestNs + "/period";
    public static final String pPeriodName = "pPeriodName";
    public static final String pPeriodClass = "pPeriodClass";
    public static final String pPeriodExams = "pPeriodExams";
    public static final String pPeriodMean = "pPeriodMean";

    public static final String examNs = scholagestNs + "/exam";
    public static final String pExamName = "pExamName";
    public static final String pExamCoeff = "pExamCoeff";
    public static final String pExamClass = "pExamClass";
    public static final String pExamGrades = "pExamGrades";;

    public static final String gradeNs = scholagestNs + "/grade";
    public static final String pGradeValue = "pGradeValue";
    public static final String pGradeExam = "pGradeExam";

    public static final String tString = scholagestNs + "#String";
    public static final String tSet = scholagestNs + "#Set";
    public static final String tCalendar = scholagestNs + "#Calendar";

    public static final String branchesBase = scholagestNs + "#branches";
    public static final String classesBase = scholagestNs + "#classes";
    public static final String examsBase = scholagestNs + "#exams";
    public static final String pageBase = scholagestNs + "#pages";
    public static final String studentsBase = scholagestNs + "#students";
    public static final String teachersBase = scholagestNs + "#teachers";
    public static final String tokenBase = scholagestNs + "#tokens";
    public static final String userBase = scholagestNs + "#users";
    public static final String yearsBase = yearNs + "#years";
}
