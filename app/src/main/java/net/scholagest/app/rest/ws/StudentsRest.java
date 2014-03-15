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
import net.scholagest.app.rest.ws.objects.StudentJson;
import net.scholagest.app.rest.ws.objects.StudentMedical;
import net.scholagest.app.rest.ws.objects.StudentPersonal;
import net.scholagest.app.rest.ws.objects.Students;

import com.google.inject.Inject;

@Path("/students")
public class StudentsRest {
    public static Map<String, StudentJson> students = new HashMap<>();

    static {
        students.put("1", new StudentJson("1", "Elodie", "Lavanchy", "1", "1", "1"));
        students.put("2", new StudentJson("2", "Thibaud", "Hottelier", "2", "2", "2"));
    }

    @Inject
    public StudentsRest() {}

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Students getStudents(@QueryParam("ids[]") final List<String> ids) {
        if (ids.isEmpty()) {
            return new Students(new ArrayList<StudentJson>(students.values()));
        } else {
            final List<StudentJson> studentsToReturn = new ArrayList<>();

            for (final StudentJson student : students.values()) {
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
        final List<StudentJson> studentsToReturn = new ArrayList<>();

        if (students.containsKey(id)) {
            studentsToReturn.add(students.get(id));
        }

        return new Students(studentsToReturn);
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveStudent(@PathParam("id") final String id, final Map<String, StudentJson> payload) {
        final StudentJson student = payload.get("student");
        mergeStudent(students.get(id), student);
    }

    private void mergeStudent(final StudentJson base, final StudentJson toMerge) {
        base.setFirstName(toMerge.getFirstName());
        base.setLastName(toMerge.getLastName());
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> createStudent(final Map<String, StudentJson> payload) {
        final Map<String, Object> result = new HashMap<>();

        final StudentJson student = payload.get("student");
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
