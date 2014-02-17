package net.scholagest.app.rest.ember;

import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ember.objects.Login;
import net.scholagest.app.rest.ember.objects.Session;

import com.google.gson.Gson;

@Path("/login")
public class LoginRest {
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Session login(final String content) {
        final Login login = new Gson().fromJson(content, Login.class);
        if (login.getUsername().equals("clavanchy")) {
            return new Session(UUID.randomUUID().toString(), "1");
        } else if (login.getUsername().equals("vparvex")) {
            return new Session(UUID.randomUUID().toString(), "2");
        } else {
            throw new WebApplicationException(401);
        }
    }
}
