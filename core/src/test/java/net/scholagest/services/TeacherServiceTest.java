package net.scholagest.services;

import java.util.UUID;

import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.managers.TeacherManager;
import net.scholagest.managers.ontology.OntologyManager;

public class TeacherServiceTest {
    public static void main(String[] args) throws Exception {
        String requestId = "teacher-" + UUID.randomUUID().toString();
        Database database = new Database(new DefaultDatabaseConfiguration());
        database.startup();
        ITeacherService teacherService = new TeacherService(database, new TeacherManager(new OntologyManager()));

        System.out.println("Teacher key: " + teacherService.createTeacher(requestId, "Normal", null));

        System.out.println("\n\nTeachers key");
        for (String key : teacherService.getTeachers(requestId)) {
            System.out.println("    " + key);
        }

        database.shutdown();
    }
}
