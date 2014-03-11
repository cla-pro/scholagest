package net.scholagest.app.rest.ws;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.objects.Login;
import net.scholagest.app.rest.ws.objects.Session;

import com.google.gson.Gson;

@Path("/login")
public class LoginRest {
    private static final Map<String, ServerSession> tokenUserMap = new HashMap<>();

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Session login(final String content) {
        final Login login = new Gson().fromJson(content, Login.class);

        if (login.hasToken()) {
            final String token = login.getToken();
            if (tokenUserMap.containsKey(token)) {
                final ServerSession serverSession = tokenUserMap.get(token);
                // TODO check expiration
                tokenUserMap.remove(token);
                return createSession(serverSession.getUserId());
            } else {
                throw new WebApplicationException(401);
            }
        } else if (login.hasUsername()) {
            if (login.getUsername().equals("clavanchy")) {
                return createSession("1");
            } else if (login.getUsername().equals("vparvex")) {
                return createSession("2");
            } else {
                throw new WebApplicationException(401);
            }
        } else {
            throw new WebApplicationException(401);
        }
    }

    private Session createSession(final String userId) {
        final String token = UUID.randomUUID().toString();
        tokenUserMap.put(token, new ServerSession(userId, new Date().getTime()));
        return new Session(token, userId);
    }

    private class ServerSession {
        private final String userId;
        private final long timestamp;

        public ServerSession(final String userId, final long timestamp) {
            this.userId = userId;
            this.timestamp = timestamp;
        }

        public String getUserId() {
            return userId;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
