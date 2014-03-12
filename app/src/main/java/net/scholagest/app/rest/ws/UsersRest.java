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
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.app.rest.ws.objects.User;
import net.scholagest.object.Teacher;
import net.scholagest.service.TeacherServiceLocal;

import com.google.inject.Inject;

@Path("/users")
public class UsersRest {
    public static Map<String, User> users = new HashMap<>();

    static {
        users.put("1", new User("1", "admin", "1", "1"));
        users.put("2", new User("2", "teacher", "2", "2"));
    }

    private final TeacherServiceLocal teacherService;

    @Inject
    public UsersRest(final TeacherServiceLocal teacherService) {
        this.teacherService = teacherService;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getUser(@PathParam("id") final String id) {
        final Map<String, Object> toReturn = new HashMap<>();

        if (users.containsKey(id)) {
            final User user = users.get(id);
            toReturn.put("user", user);

            final List<Teacher> teachers = teacherService.getTeacher(Arrays.asList(id));
            final List<TeacherJson> teachersJson = new TeacherJsonConverter().convertToTeacherJson(teachers);
            toReturn.put("teachers", teachersJson);
            // toReturn.put("teacher",
            // TeachersRest.teachers.get(user.getTeacher()));
            toReturn.put("class", ClassesRest.classes.get(user.getClazz()));
        }

        return toReturn;
    }
}
