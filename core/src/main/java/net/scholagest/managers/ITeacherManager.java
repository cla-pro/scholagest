package net.scholagest.managers;

import java.util.Set;

import net.scholagest.database.ITransaction;

public interface ITeacherManager extends IObjectManager {
	public String createTeacher(String requestId,
			ITransaction transaction) throws Exception;
	
	public Set<String> getTeachers(String requestId,
			ITransaction transaction) throws Exception;
}
