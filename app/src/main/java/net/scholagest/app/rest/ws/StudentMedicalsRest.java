package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.StudentJsonConverter;
import net.scholagest.app.rest.ws.objects.StudentMedicalJson;
import net.scholagest.object.StudentMedical;
import net.scholagest.service.StudentServiceLocal;

import com.google.inject.Inject;

@Path("/studentMedicals")
public class StudentMedicalsRest {
    // public static Map<String, StudentMedicalJson> medicals = new HashMap<>();
    //
    // static {
    // medicals.put("1", new StudentMedicalJson("1", null));
    // medicals.put("2", new StudentMedicalJson("2", null));
    // }

    private final StudentServiceLocal studentService;

    @Inject
    public StudentMedicalsRest(final StudentServiceLocal studentService) {
        this.studentService = studentService;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentMedicalJson> getMedical(@PathParam("id") final String id) {
        final StudentMedical studentMedical = studentService.getStudentMedical(id);
        final StudentMedicalJson studentMedicalJson = new StudentJsonConverter().convertToStudentMedicalJson(studentMedical);

        final Map<String, StudentMedicalJson> result = new HashMap<>();
        result.put("studentMedical", studentMedicalJson);

        return result;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentMedicalJson> saveStudentPersonal(@PathParam("id") final String id, final Map<String, StudentMedicalJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();

        final StudentMedicalJson studentMedicalJson = payload.get("studentMedical");
        final StudentMedical studentMedical = converter.convertToStudentMedical(studentMedicalJson);

        final StudentMedical updated = studentService.saveStudentMedical(id, studentMedical);
        final StudentMedicalJson updatedJson = converter.convertToStudentMedicalJson(updated);

        final Map<String, StudentMedicalJson> result = new HashMap<>();
        result.put("studentMedical", updatedJson);

        return result;
    }
}
