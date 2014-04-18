package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.ClazzJsonConverter;
import net.scholagest.app.rest.ws.converter.TeacherJsonConverter;
import net.scholagest.app.rest.ws.converter.UserJsonConverter;
import net.scholagest.app.rest.ws.objects.ClazzJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.app.rest.ws.objects.UserJson;
import net.scholagest.object.UserBlock;
import net.scholagest.service.UserServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the users. The available methods are:
 * 
 * <ul>
 *   <li>GET /{id} - to retrieve the information of a user</li>
 * </ul>
 * 
 * The creation is done through the teacher creation in the {@link TeachersRest}
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/users")
public class UsersRest {
    // @Inject
    // private TeacherServiceLocal teacherService;

    @Inject
    private UserServiceLocal userService;

    // @Inject
    // private ClazzServiceLocal clazzService;

    @Inject
    UsersRest() {}

    /**
     * Retrieve the information about a single user identified by its id.
     * 
     * @param id Id of the user to get
     * @return The user identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getUser(@PathParam("id") final String id) {
        final Map<String, Object> response = new HashMap<>();

        final UserBlock userBlock = userService.getUserBlock(id);
        if (userBlock != null) {
            final UserJson userJson = new UserJsonConverter().convertToUserJson(userBlock.getUser());
            final TeacherJson teacherJson = new TeacherJsonConverter().convertToTeacherJson(userBlock.getTeacher());
            final List<ClazzJson> clazzJsonList = new ClazzJsonConverter().convertToClazzJsonList(userBlock.getClazzes());

            response.put("user", userJson);
            response.put("teacher", teacherJson);
            response.put("classes", clazzJsonList);
        }

        // final User user = userService.getUser(id);
        // if (user != null) {
        // final UserJson userJson = new
        // UserJsonConverter().convertToUserJson(user);
        // response.put("user", userJson);
        //
        // final List<Teacher> teachers =
        // teacherService.getTeacher(Arrays.asList(user.getTeacher()));
        // final List<TeacherJson> teachersJson = new
        // TeacherJsonConverter().convertToTeacherJsonList(teachers);
        // response.put("teachers", teachersJson);
        // }

        return response;
    }

    // private ClazzJson getClazzJson(final String id) {
    // final ClazzJsonConverter converter = new ClazzJsonConverter();
    // final Clazz clazz = clazzService.getClazz(id);
    //
    // return converter.convertToClazzJson(clazz);
    // }
}
