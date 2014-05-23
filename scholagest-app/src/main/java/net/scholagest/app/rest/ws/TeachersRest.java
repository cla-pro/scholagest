package net.scholagest.app.rest.ws;

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
import javax.ws.rs.core.Response;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.TeacherJsonConverter;
import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.object.Teacher;
import net.scholagest.service.TeacherServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the teachers (see {@link TeacherJson}). The theacher contains only a link to
 * the detail information. They must be retrieved by calling the {@link TeacherDetailsRest} and webservices. The 
 * available methods are:
 * 
 * <ul>
 *   <li>GET - to retrieve the list of all the teachers</li>
 *   <li>GET /{id} - to retrieve the information of a teacher</li>
 *   <li>PUT /{id} - to update the information of a teacher</li>
 *   <li>POST - to create a new teacher. The detail information object is created within the same request</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/teachers")
public class TeachersRest {

    @Inject
    private TeacherServiceLocal teacherService;

    TeachersRest() {}

    /**
     * Retrieve a list of teachers. Either filtered by ids or the whole list. The ids
     * are specified as query parameter with the name "ids[]".
     * </p>
     * 
     * <p>
     * Examples:
     * <ul>
     *   <li>Filter the teachers by id: GET base_url/teachers?ids[]=1&ids[]=2</li>
     *   <li>Get all the teachers: GET base_url/teachers
     * </ul>
     * </p>
     * 
     * @param ids Optional parameter used to filter the list of teachers
     * @return The list of teachers optionally filtered by ids
     */
    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTeachers(@QueryParam("ids[]") final List<String> ids) {
        final List<TeacherJson> teachersJson;
        if (ids == null || ids.isEmpty()) {
            teachersJson = getAllTeachers();
        } else {
            teachersJson = getTeachersByIds(ids);
        }

        final Map<String, Object> response = new HashMap<>();
        response.put("teachers", teachersJson);

        return ResponseUtils.build200OkResponse(response);
    }

    private List<TeacherJson> getAllTeachers() {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final List<Teacher> teachers = teacherService.getTeachers();
        final List<TeacherJson> teachersJson = converter.convertToTeacherJsonList(teachers);

        return teachersJson;
    }

    private List<TeacherJson> getTeachersByIds(final List<String> ids) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final List<Teacher> teachers = teacherService.getTeacher(ids);
        final List<TeacherJson> teachersJson = converter.convertToTeacherJsonList(teachers);

        return teachersJson;
    }

    /**
     * Save the changes of the teacher into the system. This method saves only the teacher itself and not its detail
     * information (see {@link TeacherDetailsRest}).
     * 
     * @param id Id of the updated teacher
     * @param payload Teacher's information to save
     * @return The updated teacher
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response saveTeacher(@PathParam("id") final String id, final Map<String, TeacherJson> payload) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherJson teacherJson = payload.get("teacher");
        final Teacher teacher = converter.convertToTeacher(teacherJson);

        final Teacher updated = teacherService.saveTeacher(id, teacher);
        final TeacherJson updatedJson = converter.convertToTeacherJson(updated);

        final Map<String, Object> response = new HashMap<>();
        response.put("teacher", updatedJson);

        return ResponseUtils.build200OkResponse(response);
    }

    /**
     * Create a new teacher with its detail container.
     * 
     * @param payload The teacher's information to save on creation
     * @return The newly created teacher with its detail information
     */
    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createTeacher(final Map<String, TeacherJson> payload) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherJson receivedTeacherJson = payload.get("teacher");
        final Teacher receivedTeacher = converter.convertToTeacher(receivedTeacherJson);

        final Teacher createdTeacher = teacherService.createTeacher(receivedTeacher);
        final TeacherJson createdTeacherJson = converter.convertToTeacherJson(createdTeacher);
        final TeacherDetailJson createdTeacherDetailJson = converter.convertToTeacherDetailJson(createdTeacher.getDetail());

        final Map<String, Object> response = new HashMap<>();
        response.put("teacher", createdTeacherJson);
        response.put("teacherDetail", createdTeacherDetailJson);

        return ResponseUtils.build200OkResponse(response);
    }
}
