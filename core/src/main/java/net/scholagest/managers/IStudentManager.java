package net.scholagest.managers;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;

public interface IStudentManager {
	public String createStudent(String requestId,
			ITransaction transaction) throws Exception;

	public void setPersonalInfoProperties(String requestId,
			ITransaction transaction, String studentKey,
			Map<String, Object> properties) throws Exception;

	public void setMedicalInfoProperties(String requestId,
			ITransaction transaction, String studentKey,
			Map<String, Object> properties) throws Exception;

	public Set<String> getStudents(String requestId,
			ITransaction transaction) throws Exception;

	public Map<String, Object> getPersonalInfoProperties(String requestId, ITransaction transaction,
			String studentKey, Set<String> properties) throws Exception;

	public Map<String, Object> getMedicalInfoProperties(String requestId, ITransaction transaction,
			String studentKey, Set<String> properties) throws Exception;

}