package net.scholagest.managers;

public class CoreNamespace {
    public static final String scholagestNs = "http://scholagest.net";

    // Ontology
    public static final String ontologyNs = scholagestNs + "/ontology";
    public static final String ontologyBase = ontologyNs + "#ontology";
    public static final String pOntologyClassDomain = "pOntologyClassDomain";
    public static final String pOntologyClassRange = "pOntologyClassRange";

    // Types
    public static final String tGroup = scholagestNs + "#tGroup";
    public static final String tClass = scholagestNs + "#tClass";
    public static final String tTeacher = scholagestNs + "#tTeacher";
    public static final String tStudent = scholagestNs + "#tStudent";
    public static final String tStudentPersonalInfo = scholagestNs + "#tStudentPersonalInfo";
    public static final String tStudentMedicalInfo = scholagestNs + "#tStudentMedicalInfo";
    public static final String tYear = scholagestNs + "#tYear";

    // Teachers
    public static final String teacherNs = scholagestNs + "/teacher";
    public static final String teachersBase = teacherNs + "#teachers";

    private static final String pTeacherNs = "pTeacher";
    public static final String pTeacherClasses = pTeacherNs + "Classes";
    public static final String pTeacherProperties = pTeacherNs + "Properties";

    // Students
    public static final String studentNs = scholagestNs + "/student";
    public static final String studentsBase = teacherNs + "#students";

    private static final String pStudentNs = "pStudent";
    public static final String pStudentPersonalInfo = pStudentNs + "PersonalInfo";
    public static final String pStudentMedicalInfo = pStudentNs + "MedicalInfo";

    public static final String yearNs = scholagestNs + "/year";
    public static final String yearsBase = yearNs + "#years";
    public static final String pYearCurrent = "pYearCurrent";
    public static final String pYearName = "pYearName";
    public static final String pYearClasses = "pYearClasses";

    public static final String classNs = scholagestNs + "/class";
    public static final String classesBase = classNs + "#classes";
    public static final String pClassName = "pClassName";
    public static final String pClassYear = "pClassYear";

    public static final String tString = scholagestNs + "#String";
    public static final String tSet = scholagestNs + "#Set";
}
