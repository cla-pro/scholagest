package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.StudentJsonConverter;
import net.scholagest.app.rest.ws.objects.StudentClassesJson;
import net.scholagest.object.StudentClasses;
import net.scholagest.service.StudentServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the student class information (see {@link StudentClassesJson}).
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
    public Map<String, StudentClassesJson> getStudentClasses(@PathParam("id") final String id) {
        final StudentClasses studentClasses = studentService.getStudentClasses(id);
        final StudentClassesJson studentClassesJson = new StudentJsonConverter().convertToStudentClassesJson(studentClasses);

        final Map<String, StudentClassesJson> result = new HashMap<>();
        result.put("studentPersonal", studentClassesJson);

        return result;
    }
}
