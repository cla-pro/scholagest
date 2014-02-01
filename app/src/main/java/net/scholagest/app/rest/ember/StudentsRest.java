package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.objects.Student;
import net.scholagest.app.rest.ember.objects.StudentMedical;
import net.scholagest.app.rest.ember.objects.StudentPersonal;
import net.scholagest.app.rest.ember.objects.Students;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;

import com.google.inject.Inject;

@Path("/students")
public class StudentsRest extends AbstractService {
    public static Map<String, Student> students = new HashMap<>();
    public static Map<String, StudentPersonal> personals = new HashMap<>();
    public static Map<String, StudentMedical> medicals = new HashMap<>();

    static {
        students.put("1", new Student("1", "Elodie", "Lavanchy", "personal", "medical"));
        students.put("2", new Student("2", "Thibaud", "Hottelier", "personal", "medical"));

        personals.put("1", new StudentPersonal("1", "Route du Verney 8", "Perly", "1242", "Protestant"));
        personals.put("2", new StudentPersonal("2", "Post Street 711", "San Francisco", "1242", null));

        medicals.put("1", new StudentMedical("1", null));
        medicals.put("2", new StudentMedical("2", null));
    }

    @Inject
    public StudentsRest(ITeacherService teacherService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
    }

    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // public Students getStudents() {
    // return new Students(new ArrayList<Student>(students.values()));
    // }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Students getStudents(@QueryParam("ids[]") final List<String> ids) {
        if (ids.isEmpty()) {
            return new Students(new ArrayList<Student>(students.values()));
        } else {
            final List<Student> studentsToReturn = new ArrayList<>();

            for (Student student : students.values()) {
                if (ids.contains(student.getId())) {
                    studentsToReturn.add(student);
                }
            }

            return new Students(studentsToReturn);
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Students getStudents(@PathParam("id") final String id) {
        final List<Student> studentsToReturn = new ArrayList<>();

        if (students.containsKey(id)) {
            studentsToReturn.add(students.get(id));
        }

        return new Students(studentsToReturn);
    }

    @GET
    @Path("/{id}/personal")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentPersonal> getPersonal(@PathParam("id") String id) {
        StudentPersonal studentPersonal = personals.get(id);

        final Map<String, StudentPersonal> result = new HashMap<>();
        result.put("studentPersonal", studentPersonal);

        return result;
    }

    @GET
    @Path("/{id}/medical")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentMedical> getMedical(@PathParam("id") String id) {
        StudentMedical studentMedical = medicals.get(id);

        final Map<String, StudentMedical> result = new HashMap<>();
        result.put("studentMedical", studentMedical);

        return result;
    }
}
