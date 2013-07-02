package net.scholagest.initializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.database.ITransaction;
import net.scholagest.services.IStudentService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class TesterInitializer extends SystemInitializer {
    private final IStudentService studentService;
    private final ITeacherService teacherService;
    private final IUserService userService;

    public static void main(String[] args) throws Exception {
        String baseFolder = "initializer/tester/";
        if (args.length > 0) {
            baseFolder = args[0];
        }

        Injector injector = Guice.createInjector(new GuiceContext(), new ShiroGuiceContext());

        org.apache.shiro.mgt.SecurityManager securityManager = injector.getInstance(org.apache.shiro.mgt.SecurityManager.class);
        SecurityUtils.setSecurityManager(securityManager);

        injector.getInstance(TesterInitializer.class).initialize(baseFolder);
    }

    @Inject
    public TesterInitializer(IStudentService studentService, ITeacherService teacherService, IUserService userService) {
        super.setKeyspace(ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        this.studentService = studentService;
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @Override
    protected void fillDatabase(ITransaction transaction) throws Exception {
        super.fillDatabase(transaction);

        createTestObjects();
    }

    private void createTestObjects() throws Exception {
        authenticateAsAdmin();
        createStudents();
        createTeachers();
    }

    private void authenticateAsAdmin() throws Exception {
        Subject subject = userService.authenticateWithUsername("admin", "admin");
        ScholagestThreadLocal.setSubject(subject);
    }

    private void createStudents() throws Exception {
        List<Map<String, Object>> studentList = readFileAndConvertToMap(getBaseFolder() + "students.sga");

        for (Map<String, Object> studentProperties : studentList) {
            studentService.createStudent(studentProperties);
        }
    }

    private void createTeachers() throws Exception {
        List<Map<String, Object>> teacherList = readFileAndConvertToMap(getBaseFolder() + "teachers.sga");

        for (Map<String, Object> teacherProperties : teacherList) {
            teacherService.createTeacher("", teacherProperties);
        }
    }

    private List<Map<String, Object>> readFileAndConvertToMap(String filename) throws IOException {
        List<List<String>> content = readFile(filename);

        List<Map<String, Object>> elementList = new ArrayList<>();

        for (List<String> contentElement : content) {
            Map<String, Object> element = new HashMap<String, Object>();

            for (int i = 0; i < contentElement.size(); i += 2) {
                element.put(contentElement.get(i), contentElement.get(i + 1));
            }

            elementList.add(element);
        }

        return elementList;
    }
}
