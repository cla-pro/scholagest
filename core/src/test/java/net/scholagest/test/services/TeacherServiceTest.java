package net.scholagest.test.services;

import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.managers.TeacherManager;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.TeacherService;

public class TeacherServiceTest {
	public static void main(String[] args) throws Exception {
		Database database = new Database(new DefaultDatabaseConfiguration());
		database.startup();
		ITeacherService teacherService = new TeacherService(database,
				new TeacherManager());
		
		System.out.println("Teacher key: " + teacherService.createTeacher("Normal", null));
		
		System.out.println("\n\nTeachers key");
		for (String key : teacherService.getTeachers()) {
			System.out.println("    " + key);
		}
		
		database.shutdown();
	}
}
