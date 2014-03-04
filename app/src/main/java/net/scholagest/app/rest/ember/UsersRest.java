package net.scholagest.app.rest.ember;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.User;

@Path("/users")
public class UsersRest {
    public static Map<String, User> users = new HashMap<>();

    static {
        users.put("1", new User("1", "teacher", "1", "1"));
        users.put("2", new User("2", "teacher", "2", "2"));
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
            toReturn.put("teacher", TeachersRest.teachers.get(user.getTeacher()));
            toReturn.put("class", ClassesRest.classes.get(user.getClazz()));
        }

        return toReturn;
    }
}
