package net.scholagest.managers;

import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;

public class StudentManager {
	private final static String MEDICAL_INFO_SUFFIX = "#medicalInfo";
	private final static String PERSONAL_INFO_SUFFIX = "#personalInfo";
	private final static String OLD_SCHOLAR_INFO_SUFFIX = "#oldScholarInfo";
	
	public String createStudent(String requestId, ITransaction transaction)
			throws Exception {
		String id = UUID.randomUUID().toString();
		String studentKey = CoreNamespace.studentNs + 
				"#" + id;
		String studentBase = CoreNamespace.studentNs + "/" + id;
		transaction.insert(CoreNamespace.studentsBase, studentKey,
				studentKey, null);
		
		String personalInfoKey = studentBase + PERSONAL_INFO_SUFFIX;
		transaction.insert(studentKey, CoreNamespace.pStudentPersonalInfo,
				personalInfoKey, null);
		
		String medicalInfoKey = studentBase + MEDICAL_INFO_SUFFIX;
		transaction.insert(studentKey, CoreNamespace.pStudentMedicalInfo,
				medicalInfoKey, null);
		
		String oldScholarInfoKey = studentBase + OLD_SCHOLAR_INFO_SUFFIX;
		transaction.insert(studentKey,
				CoreNamespace.pStudentOldScholarInfo,
				oldScholarInfoKey, null);
		
		return studentKey;
	}

	public Set<String> getStudents(String requestId,
			ITransaction transaction) throws Exception {
		return transaction.getColumns(CoreNamespace.studentsBase);
	}
}
