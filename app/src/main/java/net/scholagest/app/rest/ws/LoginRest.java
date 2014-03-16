package net.scholagest.app.rest.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.objects.Login;
import net.scholagest.app.rest.ws.objects.SessionJson;
import net.scholagest.exception.ScholagestException;
import net.scholagest.object.SessionInfo;
import net.scholagest.service.SessionServiceLocal;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/login")
public class LoginRest {
    // private static final Map<String, ServerSession> tokenUserMap = new
    // HashMap<>();

    private final SessionServiceLocal loginService;

    @Inject
    public LoginRest(final SessionServiceLocal loginService) {
        this.loginService = loginService;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public SessionJson login(final String content) {
        final Login login = new Gson().fromJson(content, Login.class);

        if (login.hasToken()) {
            try {
                final SessionInfo sessionInfo = loginService.authenticateWithSessionId(login.getToken());
                return new SessionJson(sessionInfo.getToken(), sessionInfo.getUser().getId());
            } catch (final ScholagestException e) {
                throw new WebApplicationException(e, 401);
            }
            // final String token = login.getToken();
            // if (tokenUserMap.containsKey(token)) {
            // final ServerSession serverSession = tokenUserMap.get(token);
            // // TODO check expiration
            // tokenUserMap.remove(token);
            // return createSession(serverSession.getUserId());
            // } else {
            // throw new WebApplicationException(401);
            // }
        } else if (login.hasUsername()) {
            try {
                final SessionInfo sessionInfo = loginService.authenticateWithUsername(login.getUsername(), login.getPassword());
                return new SessionJson(sessionInfo.getToken(), sessionInfo.getUser().getId());
            } catch (final ScholagestException e) {
                throw new WebApplicationException(e, 401);
            }

            // if (login.getUsername().equals("clavanchy")) {
            // return createSession("1");
            // } else if (login.getUsername().equals("vparvex")) {
            // return createSession("2");
            // } else {
            // throw new WebApplicationException(401);
            // }
        } else {
            throw new WebApplicationException(401);
        }
    }

    // private SessionJson createSession(final String userId) {
    // final String token = UUID.randomUUID().toString();
    // tokenUserMap.put(token, new ServerSession(userId, new Date().getTime()));
    // return new SessionJson(token, userId);
    // }
    //
    // private class ServerSession {
    // private final String userId;
    // private final long timestamp;
    //
    // public ServerSession(final String userId, final long timestamp) {
    // this.userId = userId;
    // this.timestamp = timestamp;
    // }
    //
    // public String getUserId() {
    // return userId;
    // }
    //
    // public long getTimestamp() {
    // return timestamp;
    // }
    // }
}
