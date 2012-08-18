package net.scholagest.services;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IStudentManager;

import com.google.inject.Inject;

public class StudentService implements IStudentService {
	private IDatabase database = null;
	private IStudentManager studentManager;

	@Inject
	public StudentService(IDatabase database, IStudentManager studentManager) {
		this.database = database;
		this.studentManager = studentManager;
	}

	@Override
	public String createStudent(String requestId,
			Map<String, Object> personalInfo) throws Exception {
		String studentKey = null;

		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			studentKey = studentManager.createStudent(requestId, transaction);

			if (personalInfo != null) {
				studentManager.setPersonalInfoProperties(requestId, transaction, studentKey, personalInfo);
			}

			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}

		return studentKey;
	}

	@Override
	public void updateStudentInfo(String requestId,
			String studentKey,
			Map<String, Object> personalInfo, Map<String, Object> medicalInfo)
					throws Exception {
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			studentManager.setPersonalInfoProperties(requestId, transaction, studentKey, personalInfo);
			studentManager.setMedicalInfoProperties(requestId, transaction, studentKey, medicalInfo);
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getStudentInfo(String requestId,
			String studentKey, Set<String> properties) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
