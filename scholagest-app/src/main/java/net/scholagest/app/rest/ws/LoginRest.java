package net.scholagest.app.rest.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.scholagest.app.rest.ws.objects.LoginJson;
import net.scholagest.app.rest.ws.objects.SessionJson;
import net.scholagest.exception.ScholagestException;
import net.scholagest.object.SessionInfo;
import net.scholagest.service.SessionServiceLocal;

import com.google.gson.Gson;
import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the login action. This action is provided
 * as a POST method. For the login to succeed, the request's payload must contain either a known and correct
 * username/password pair or a valid token (existing and not expired).
 * 
 * @author CLA
 * @since 0.13.0
 */
@Path("/login")
public class LoginRest {

    @Inject
    private SessionServiceLocal loginService;

    LoginRest() {}

    /**
     * Check the authentication information and in case of success, create a new session. The authentication
     * information can be either a pair username/password or a session id (stored in the browser's cookies for instance).
     * 
     * @param content The authentication information.
     * @return The session information
     * @throws AuthenticationException with code 401 (Unauthorized) if the authentication information are incorrect
     */
    @POST
    // @Produces(MediaType.APPLICATION_JSON)
    public Response login(final String content) {
        final LoginJson login = new Gson().fromJson(content, LoginJson.class);

        if (login.hasToken()) {
            try {
                final SessionInfo sessionInfo = loginService.authenticateWithSessionId(login.getToken());
                final SessionJson sessionJson = new SessionJson(sessionInfo.getToken(), sessionInfo.getUser().getId());
                return buildOk(sessionJson);
            } catch (final ScholagestException e) {
                // throw new WebApplicationException(e, 401);
                return buildUnauthorized();
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
                final SessionJson sessionJson = new SessionJson(sessionInfo.getToken(), sessionInfo.getUser().getId());
                return buildOk(sessionJson);
            } catch (final ScholagestException e) {
                // throw new WebApplicationException(e, 401);
                return buildUnauthorized();
            }
        } else {
            // throw new WebApplicationException(401);
            return buildUnauthorized();
        }
    }

    private Response buildOk(final SessionJson sessionJson) {
        final String json = new Gson().toJson(sessionJson);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    private Response buildUnauthorized() {
        return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", "Basic realm=\"test\"").build();
    }
}
