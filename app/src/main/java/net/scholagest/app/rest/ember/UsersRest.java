package net.scholagest.app.rest.ember;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
        users.put("1", new User("1", "1"));
        users.put("2", new User("2", "2"));
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<User>> getUser(@PathParam("id") String id) {
        final Map<String, List<User>> toReturn = new HashMap<>();

        if (users.containsKey(id)) {
            toReturn.put("users", Arrays.asList(users.get(id)));
        }

        return toReturn;
    }
}
