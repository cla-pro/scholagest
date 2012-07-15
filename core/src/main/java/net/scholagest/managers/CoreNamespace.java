package net.scholagest.managers;

public class CoreNamespace {
	protected static final String scholagestNs = "http://scholagest.net";
	
	//Ontology
	public static final String ontologyNs = scholagestNs + "/ontology";
	public static final String ontologyBase = ontologyNs + "#ontology";
	
	//Teachers
	public static final String teacherNs = scholagestNs + "/teacher";
	public static final String teachersBase = teacherNs + "#teachers";
	
	private static final String pTeacherNs = "pTeacher";
	public static final String pTeacherClasses = pTeacherNs + "Classes";
	public static final String pTeacherProperties = pTeacherNs + "Properties";

	//Students
	public static final String studentNs = scholagestNs + "/student";
	public static final String studentsBase = teacherNs + "#students";
	
	private static final String pStudentNs = "pStudent";
	public static final String pStudentPersonalInfo =
			pStudentNs + "PersonalInfo";
	public static final String pStudentMedicalInfo =
			pStudentNs + "MedicalInfo";
	public static final String pStudentCurrentScholarInfo =
			pStudentNs + "CurrentScholarInfo";
	public static final String pStudentOldScholarInfo =
			pStudentNs + "OldScholarInfo";
}
