package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.StudentJsonConverter;
import net.scholagest.app.rest.ws.objects.StudentPersonalJson;
import net.scholagest.object.StudentPersonal;
import net.scholagest.service.StudentServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the student personal information (see {@link StudentPersonalJson}).
 * The available methods are:
 * 
 * <ul>
 *   <li>GET /{id} - to retrieve the personal information of a student</li>
 *   <li>PUT /{id} - to update the personal information of a student</li>
 * </ul>
 * 
 * The creation is done through the student creation in the {@link StudentsRest}
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/studentPersonals")
public class StudentPersonalsRest {

    @Inject
    private StudentServiceLocal studentService;

    StudentPersonalsRest() {}

    /**
     * Retrieve the personal information about a single student identified by its id.
     * 
     * @param id Id of the student personal information to get
     * @return The student personal information identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentPersonal(@PathParam("id") final String id) {
        final StudentPersonal studentPersonal = studentService.getStudentPersonal(id);
        final StudentPersonalJson studentPersonalJson = new StudentJsonConverter().convertToStudentPersonalJson(studentPersonal);

        final Map<String, StudentPersonalJson> response = new HashMap<>();
        response.put("studentPersonal", studentPersonalJson);

        return ResponseUtils.build200OkResponse(response);
    }

    /**
     * Save the changes of the student's personal information into the system.
     * 
     * @param id Id of the updated student's personal information
     * @param payload Student's personal information to save
     * @return The updated student personal information
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveStudentPersonal(@PathParam("id") final String id, final Map<String, StudentPersonalJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();

        final StudentPersonalJson studentPersonalJson = payload.get("studentPersonal");
        final StudentPersonal studentPersonal = converter.convertToStudentPersonal(studentPersonalJson);

        final StudentPersonal updated = studentService.saveStudentPersonal(id, studentPersonal);
        final StudentPersonalJson updatedJson = converter.convertToStudentPersonalJson(updated);

        final Map<String, StudentPersonalJson> response = new HashMap<>();
        response.put("studentPersonal", updatedJson);

        return ResponseUtils.build200OkResponse(response);
    }
}
