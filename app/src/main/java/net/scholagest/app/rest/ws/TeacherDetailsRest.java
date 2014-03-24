package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.TeacherJsonConverter;
import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.object.TeacherDetail;
import net.scholagest.service.TeacherServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the teacher detail information (see {@link TeacherDetailJson}).
 * The available methods are:
 * 
 * <ul>
 *   <li>GET /{id} - to retrieve the detail information of a teacher</li>
 *   <li>PUT /{id} - to update the detail information of a teacher</li>
 * </ul>
 * 
 * The creation is done through the teacher creation in the {@link TeachersRest}
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/teacherDetails")
public class TeacherDetailsRest {

    @Inject
    private TeacherServiceLocal teacherService;

    TeacherDetailsRest() {}

    /**
     * Retrieve the detail information about a single teacher identified by its id.
     * 
     * @param id Id of the teacher detail information to get
     * @return The teacher detail information identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getTeacherDetail(@PathParam("id") final String id) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherDetail teacherDetail = teacherService.getTeacherDetail(id);
        final TeacherDetailJson teacherDetailJson = converter.convertToTeacherDetailJson(teacherDetail);

        final Map<String, Object> response = new HashMap<>();
        response.put("teacherDetail", teacherDetailJson);

        return response;
    }

    /**
     * Save the changes of the teacher's detail information into the system.
     * 
     * @param id Id of the updated teacher's detail information
     * @param payload teacher's detail information to save
     * @return The updated teacher detail information
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveTeacherDetail(@PathParam("id") final String id, final Map<String, TeacherDetailJson> payload) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherDetailJson teacherDetailJson = payload.get("teacherDetail");
        final TeacherDetail teacherDetail = converter.convertToTeacherDetail(teacherDetailJson);

        teacherService.saveTeacherDetail(id, teacherDetail);
    }
}
