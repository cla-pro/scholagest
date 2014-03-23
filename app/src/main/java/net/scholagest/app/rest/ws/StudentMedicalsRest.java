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

/**
 * Set methods available for rest calls (WebService) to handle the student medical information (see {@link StudentMedicalJson}).
 * The available methods are:
 * 
 * <ul>
 *   <li>GET /{id} - to retrieve the medical information of a student</li>
 *   <li>PUT /{id} - to update the medical information of a student</li>
 * </ul>
 * 
 * The creation is done through the student creation in the {@link StudentsRest}
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/studentMedicals")
public class StudentMedicalsRest {

    private final StudentServiceLocal studentService;

    @Inject
    public StudentMedicalsRest(final StudentServiceLocal studentService) {
        this.studentService = studentService;
    }

    /**
     * Retrieve the medical information about a single student identified by its id.
     * 
     * @param id Id of the student medical information to get
     * @return The student medical information identified by id
     */
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

    /**
     * Save the changes of the student's medical information into the system.
     * 
     * @param id Id of the updated student's medical information
     * @param payload Student's medical information to save
     * @return The updated student medical information
     */
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
