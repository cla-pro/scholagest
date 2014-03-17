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

/**
 * Set methods available for rest calls (WebService) to handle the students. The student contains only links to
 * the personal and medical information. They must be retrieved by calling the {@link StudentPersonalsRest} and
 * the {@link StudentMedicalRest} webservices. The vailable methods are:
 * 
 * <ul>
 *   <li>GET - to retrieve the list of all the students</li>
 *   <li>GET ids[] - to retrieve a list of students filtered by the ids</li>
 *   <li>GET /{id} - to retrieve the information of a student</li>
 *   <li>PUT /{id} - to update the information of a student</li>
 *   <li>POST - to create a new student. The personal and medical information objects are created within the same request</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.13.0
 */
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

    /**
     * <p>
     * Retrieve a list of students. Either filtered by ids or the whole list. The ids
     * are specified as query parameter with the name "ids[]".
     * </p>
     * 
     * <p>
     * Examples:
     * <ul>
     *   <li>Filter the students by id: GET base_url/students?ids[]=1&ids[]=2</li>
     *   <li>Get all the students: GET base_url/students
     * </ul>
     * </p>
     * 
     * @param ids Optional parameter used to filter the list of students
     * @return The list of students optionally filtered by ids
     */
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

    /**
     * Retrieve the information about a single student identified by its id. This method returns only the student itself and not its personal
     * or medical information (see {@link StudentPersonalsRest} and {@link StudentMedicalRest})
     * 
     * @param id Id of the student to get
     * @return The student identified by id
     */
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

    /**
     * Save the changes of the student into the system. This method saves only the student itself and not its personal
     * or medical information (see {@link StudentPersonalsRest} and {@link StudentMedicalRest}).
     * 
     * @param id Id of the updated student
     * @param payload Student's information to save
     * @return The updated student
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> saveStudent(@PathParam("id") final String id, final Map<String, StudentJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();
        final StudentJson studentJson = payload.get("student");
        final Student student = converter.convertToStudent(studentJson);

        final Student updated = studentService.saveStudent(id, student);
        final StudentJson updatedJson = converter.convertToStudentJson(updated);

        final Map<String, Object> response = new HashMap<>();
        response.put("student", updatedJson);

        return response;
    }

    /**
     * Create a new student with its student personal and medical container.
     * 
     * @param payload The student's information to save on creation
     * @return The newly created student with its personal and medical information
     */
    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> createStudent(final Map<String, StudentJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();

        final StudentJson studentJson = payload.get("student");
        final Student student = converter.convertToStudent(studentJson);

        final Student createdStudent = studentService.createStudent(student);

        final StudentJson createdStudentJson = converter.convertToStudentJson(createdStudent);
        final StudentPersonalJson createStudentPersonalJson = converter.convertToStudentPersonalJson(createdStudent.getStudentPersonal());
        final StudentMedicalJson createStudentMedicalJson = converter.convertToStudentMedicalJson(createdStudent.getStudentMedical());

        final Map<String, Object> result = new HashMap<>();
        result.put("student", createdStudentJson);
        result.put("studentMedical", createStudentMedicalJson);
        result.put("studentPersonal", createStudentPersonalJson);

        return result;
    }
}
