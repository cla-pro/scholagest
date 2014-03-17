package net.scholagest.app.rest.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.StudentJsonConverter;
import net.scholagest.app.rest.ws.objects.StudentJson;
import net.scholagest.app.rest.ws.objects.StudentMedicalJson;
import net.scholagest.app.rest.ws.objects.StudentPersonalJson;
import net.scholagest.object.Student;
import net.scholagest.service.StudentServiceLocal;

import com.google.inject.Inject;

@Path("/students")
public class StudentsRest {
    // public static Map<String, StudentJson> students = new HashMap<>();
    //
    // static {
    // students.put("1", new StudentJson("1", "Elodie", "Lavanchy", "1", "1",
    // "1"));
    // students.put("2", new StudentJson("2", "Thibaud", "Hottelier", "2", "2",
    // "2"));
    // }

    private final StudentServiceLocal studentService;

    @Inject
    public StudentsRest(final StudentServiceLocal studentService) {
        this.studentService = studentService;
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getStudents(@QueryParam("ids[]") final List<String> ids) {
        final StudentJsonConverter converter = new StudentJsonConverter();
        final Map<String, Object> response = new HashMap<>();

        if (ids.isEmpty()) {
            final List<Student> studentList = studentService.getStudents();
            final List<StudentJson> studentJsonList = converter.convertToStudentJson(studentList);

            response.put("students", studentJsonList);
        } else {
            final List<StudentJson> studentJsonList = new ArrayList<>();

            for (final String id : ids) {
                final Student student = studentService.getStudent(id);
                if (student != null) {
                    studentJsonList.add(converter.convertToStudentJson(student));
                }
            }

            response.put("students", studentJsonList);
        }

        return response;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getStudent(@PathParam("id") final String id) {
        final StudentJsonConverter converter = new StudentJsonConverter();
        final Map<String, Object> response = new HashMap<>();

        final Student student = studentService.getStudent(id);
        if (student != null) {
            final StudentJson studentJson = converter.convertToStudentJson(student);
            response.put("student", studentJson);
        }

        return response;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveStudent(@PathParam("id") final String id, final Map<String, StudentJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();
        final StudentJson studentJson = payload.get("student");

        studentService.saveStudent(id, converter.convertToStudent(studentJson));
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> createStudent(final Map<String, StudentJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();
        final Map<String, Object> result = new HashMap<>();

        final StudentJson studentJson = payload.get("student");
        final Student createdStudent = studentService.createStudent(converter.convertToStudent(studentJson));
        final StudentJson createdStudentJson = converter.convertToStudentJson(createdStudent);
        final StudentPersonalJson createStudentPersonalJson = converter.convertToStudentPersonalJson(createdStudent.getStudentPersonal());
        final StudentMedicalJson createStudentMedicalJson = converter.convertToStudentMedicalJson(createdStudent.getStudentMedical());

        result.put("student", createdStudentJson);
        result.put("studentMedical", createStudentMedicalJson);
        result.put("studentPersonal", createStudentPersonalJson);

        return result;
    }
}
