package net.scholagest.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.ITransaction;

public class StudentManager extends ObjectManager implements IStudentManager {
	private final static String MEDICAL_INFO_SUFFIX = "#medicalInfo";
	private final static String PERSONAL_INFO_SUFFIX = "#personalInfo";
	
	@Override
	public String createStudent(String requestId, ITransaction transaction) throws Exception {
		String studentUUID = UUID.randomUUID().toString();
		String studentKey = generateStudentKey(studentUUID);
		super.createObject(requestId, transaction, studentKey, CoreNamespace.tStudent);

		transaction.insert(CoreNamespace.studentsBase, studentKey,
				studentKey, null);
		
		String studentBase = generateStudentBase(studentUUID);
		String personalInfoKey = super.createObject(requestId, transaction,
				generatePersonalInfoKey(studentBase), CoreNamespace.tStudentPersonalInfo);
		String medicalInfoKey = super.createObject(requestId, transaction,
				generateMedicalInfoKey(studentBase), CoreNamespace.tStudentMedicalInfo);
		
		Map<String, Object> properties = new HashMap<>();
		properties.put(CoreNamespace.pStudentPersonalInfo, personalInfoKey);
		properties.put(CoreNamespace.pStudentMedicalInfo, medicalInfoKey);
		super.setObjectProperties(requestId, transaction, studentKey, properties);
		
		return studentKey;
	}
	
	@Override
	public void setPersonalInfoProperties(String requestId, ITransaction transaction,
			String studentKey, Map<String, Object> properties) throws Exception {
		String personalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentPersonalInfo, null);
		super.setObjectProperties(requestId, transaction, personalInfoKey, properties);
	}
	
	@Override
	public void setMedicalInfoProperties(String requestId, ITransaction transaction,
			String studentKey, Map<String, Object> properties) throws Exception {
		String medicalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentMedicalInfo, null);
		super.setObjectProperties(requestId, transaction, medicalInfoKey, properties);
	}
	
	@Override
	public Map<String, Object> getPersonalInfoProperties(String requestId, ITransaction transaction,
			String studentKey, Set<String> properties) throws Exception {
		String personalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentPersonalInfo, null);
		return super.getObjectProperties(requestId, transaction, personalInfoKey, properties);
	}
	
	@Override
	public Map<String, Object> getMedicalInfoProperties(String requestId, ITransaction transaction,
			String studentKey, Set<String> properties) throws Exception {
		String medicalInfoKey = (String) transaction.get(studentKey, CoreNamespace.pStudentMedicalInfo, null);
		return super.getObjectProperties(requestId, transaction, medicalInfoKey, properties);
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
	public Set<String> getStudents(String requestId,
			ITransaction transaction) throws Exception {
		return transaction.getColumns(CoreNamespace.studentsBase);
	}
}
