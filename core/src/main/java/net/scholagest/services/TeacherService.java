package net.scholagest.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ITeacherManager;

import com.google.inject.Inject;

public class TeacherService implements ITeacherService {
	private IDatabase database = null;
	private ITeacherManager teacherManager = null;
	
	@Inject
	public TeacherService(IDatabase database, ITeacherManager teacherManager) {
		this.database = database;
		this.teacherManager = teacherManager;
	}

	@Override
	public Set<String> getTeacherTypes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createTeacher(String teacherType,
			Map<String, Object> teacherProperties) throws Exception {
		String teacherKey = null;
		String requestId = UUID.randomUUID().toString();
		
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			teacherKey = this.teacherManager.createTeacher(requestId,
					transaction);
			if (teacherProperties != null)
				this.teacherManager.setObjectProperties(requestId, transaction,
						teacherKey, teacherProperties);
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return teacherKey;
	}
	
	@Override
	public Set<String> getTeachers() throws Exception {
		String requestId = UUID.randomUUID().toString();
		Set<String> teachers = null;
		
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			teachers = new HashSet<String>();
			for (String col : transaction.getColumns(CoreNamespace.teachersBase)) {
				teachers.add((String) transaction.get(CoreNamespace.teachersBase, col, null));
			}
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return teachers;
	}

	@Override
	public Map<String, Map<String, Object>> getTeachersWithProperties(Set<String> propertiesName) throws Exception {
		String requestId = UUID.randomUUID().toString();
		Map<String, Map<String, Object>> teachers = null;
		
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			teachers = new HashMap<String, Map<String, Object>>();
			for (String col : transaction.getColumns(CoreNamespace.teachersBase)) {
				String teacherKey = (String) transaction.get(CoreNamespace.teachersBase, col, null);
				//teachers.add((String) transaction.get(CoreNamespace.teachersBase, col, null));
				Map<String, Object> info =
						this.teacherManager.getObjectProperties(requestId,
								transaction, teacherKey, propertiesName);
				teachers.put(teacherKey, info);
			}
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return teachers;
	}

	@Override
	public Set<String> getTeacherClasses(String teacherKey) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTeacherInfo(String teacherKey, Map<String, Object> properties)
			throws Exception {
		String requestId = UUID.randomUUID().toString();

		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			System.out.println("Set teacher properties");
			this.teacherManager.setObjectProperties(requestId, transaction, teacherKey, properties);
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getTeacherInfo(String teacherKey,
			Set<String> propertiesName) throws Exception {
		String requestId = UUID.randomUUID().toString();

		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			Map<String, Object> info =
					this.teacherManager.getObjectProperties(requestId,
							transaction, teacherKey, propertiesName);
			
			transaction.commit();
			return info;
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return new HashMap<>();
	}

}
