package net.scholagest.app.rest.ws;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.TeacherJsonConverter;
import net.scholagest.app.rest.ws.converter.UserJsonConverter;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.app.rest.ws.objects.UserJson;
import net.scholagest.object.Teacher;
import net.scholagest.object.User;
import net.scholagest.service.TeacherServiceLocal;
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
    private final TeacherServiceLocal teacherService;
    private final UserServiceLocal userService;

    @Inject
    public UsersRest(final TeacherServiceLocal teacherService, final UserServiceLocal userService) {
        this.teacherService = teacherService;
        this.userService = userService;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getUser(@PathParam("id") final String id) {
        final Map<String, Object> toReturn = new HashMap<>();

        final User user = userService.getUser(id);
        if (user != null) {
            final UserJson userJson = new UserJsonConverter().convertToUserJson(user);
            toReturn.put("user", userJson);

            final List<Teacher> teachers = teacherService.getTeacher(Arrays.asList(user.getTeacherId()));
            final List<TeacherJson> teachersJson = new TeacherJsonConverter().convertToTeacherJson(teachers);
            toReturn.put("teachers", teachersJson);

            // TODO class is null -> go get it
            // toReturn.put("class",
            // ClassesRest.classes.get(userJson.getClazz()));
        }

        return toReturn;
    }
}
