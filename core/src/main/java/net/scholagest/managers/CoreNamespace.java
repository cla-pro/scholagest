package net.scholagest.managers;

public class CoreNamespace {
	public static final String scholagestNs = "http://scholagest.net";
	
	//Ontology
	public static final String ontologyNs = scholagestNs + "/ontology";
	public static final String ontologyBase = ontologyNs + "#ontology";
	
	//Types
	public static final String tGroup = scholagestNs + "#tGroup";
	public static final String tTeacher = scholagestNs + "#tTeacher";
	public static final String tStudent = scholagestNs + "#tStudent";
	public static final String tStudentPersonalInfo = scholagestNs + "#tStudentPersonalInfo";
	public static final String tStudentMedicalInfo = scholagestNs + "#tStudentMedicalInfo";
	
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
}
