package net.scholagest.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	public String createTeacher(String requestId, String teacherType,
			Map<String, Object> teacherProperties) throws Exception {
		String teacherKey = null;
		
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			teacherKey = this.teacherManager.createTeacher(requestId,
					transaction);
			if (teacherProperties != null)
				this.teacherManager.setTeacherProperties(requestId, transaction,
						teacherKey, teacherProperties);
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
		
		return teacherKey;
	}
	
	@Override
	public Set<String> getTeachers(String requestId) throws Exception {
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
	public Map<String, Map<String, Object>> getTeachersWithProperties(String requestId,
			Set<String> propertiesName) throws Exception {
		Map<String, Map<String, Object>> teachers = null;
		
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			teachers = new HashMap<String, Map<String, Object>>();
			for (String col : transaction.getColumns(CoreNamespace.teachersBase)) {
				String teacherKey = (String) transaction.get(CoreNamespace.teachersBase, col, null);
				//teachers.add((String) transaction.get(CoreNamespace.teachersBase, col, null));
				Map<String, Object> info =
						this.teacherManager.getTeacherProperties(requestId,
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
	public Set<String> getTeacherClasses(String requestId, String teacherKey) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTeacherInfo(String requestId, String teacherKey, Map<String, Object> properties)
			throws Exception {
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			System.out.println("Set teacher properties");
			this.teacherManager.setTeacherProperties(requestId, transaction, teacherKey, properties);
			
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			e.printStackTrace();
		}
	}

	@Override
	public Map<String, Object> getTeacherInfo(String requestId, String teacherKey,
			Set<String> propertiesName) throws Exception {
		ITransaction transaction = this.database.getTransaction(
				SecheronNamespace.SECHERON_KEYSPACE);
		try {
			Map<String, Object> info =
					this.teacherManager.getTeacherProperties(requestId,
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
