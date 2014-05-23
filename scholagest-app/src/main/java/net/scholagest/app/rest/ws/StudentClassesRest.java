package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.StudentJsonConverter;
import net.scholagest.app.rest.ws.objects.StudentClassJson;
import net.scholagest.object.StudentClass;
import net.scholagest.service.StudentServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the student class information (see {@link StudentClassJson}).
 * The available methods are:
 * 
 * <ul>
 *   <li>GET /{id} - to retrieve the class information of a student</li>
 * </ul>
 * 
 * The creation is done through the student creation in the {@link StudentsRest}
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/studentClasses")
public class StudentClassesRest {

    @Inject
    private StudentServiceLocal studentService;

    StudentClassesRest() {}

    /**
     * Retrieve the class information about a single student identified by its id.
     * 
     * @param id Id of the student class information to get
     * @return The student class information identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStudentClasses(@PathParam("id") final String id) {
        final StudentClass studentClasses = studentService.getStudentClasses(id);
        final StudentClassJson studentClassesJson = new StudentJsonConverter().convertToStudentClassJson(studentClasses);

        final Map<String, StudentClassJson> response = new HashMap<>();
        response.put("studentClass", studentClassesJson);

        return ResponseUtils.build200OkResponse(response);
    }
}
