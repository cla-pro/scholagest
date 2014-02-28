package net.scholagest.app.rest.ember;

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

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Student;
import net.scholagest.app.rest.ember.objects.StudentMedical;
import net.scholagest.app.rest.ember.objects.StudentPersonal;
import net.scholagest.app.rest.ember.objects.Students;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/students")
public class StudentsRest extends AbstractService {
    public static Map<String, Student> students = new HashMap<>();

    static {
        students.put("1", new Student("1", "Elodie", "Lavanchy", "1", "1", "1"));
        students.put("2", new Student("2", "Thibaud", "Hottelier", "2", "2", "2"));
    }

    @Inject
    public StudentsRest(final IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Students getStudents(@QueryParam("ids[]") final List<String> ids) {
        if (ids.isEmpty()) {
            return new Students(new ArrayList<Student>(students.values()));
        } else {
            final List<Student> studentsToReturn = new ArrayList<>();

            for (final Student student : students.values()) {
                if (ids.contains(student.getId())) {
                    studentsToReturn.add(student);
                }
            }

            return new Students(studentsToReturn);
        }
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Students getStudent(@PathParam("id") final String id) {
        final List<Student> studentsToReturn = new ArrayList<>();

        if (students.containsKey(id)) {
            studentsToReturn.add(students.get(id));
        }

        return new Students(studentsToReturn);
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveStudent(@PathParam("id") final String id, final Map<String, Student> payload) {
        final Student student = payload.get("student");
        mergeStudent(students.get(id), student);
    }

    private void mergeStudent(final Student base, final Student toMerge) {
        base.setFirstName(toMerge.getFirstName());
        base.setLastName(toMerge.getLastName());
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> createStudent(final Map<String, Student> payload) {
        final Map<String, Object> result = new HashMap<>();

        final Student student = payload.get("student");
        final String id = IdHelper.getNextId(students.keySet());
        student.setId(id);
        students.put(id, student);

        final String medicalId = IdHelper.getNextId(StudentMedicalsRest.medicals.keySet());
        final StudentMedical studentMedical = new StudentMedical(medicalId, null);
        StudentMedicalsRest.medicals.put(medicalId, studentMedical);

        final String personalId = IdHelper.getNextId(StudentPersonalsRest.personals.keySet());
        final StudentPersonal studentPersonal = new StudentPersonal(personalId, null, null, null, null);
        StudentPersonalsRest.personals.put(personalId, studentPersonal);

        student.setMedical(medicalId);
        student.setPersonal(personalId);

        result.put("student", student);
        result.put("studentMedical", studentMedical);
        result.put("studentPersonal", studentPersonal);

        return result;
    }
}
