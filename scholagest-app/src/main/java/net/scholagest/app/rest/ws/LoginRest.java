package net.scholagest.app.rest.ws;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import net.scholagest.app.rest.ws.objects.LoginJson;
import net.scholagest.app.rest.ws.objects.SessionJson;
import net.scholagest.exception.AuthorizationScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
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
     * @throws AuthorizationScholagestException If the login information are invalid
     */
    @POST
    public Response login(final String content) {
        final LoginJson login = new Gson().fromJson(content, LoginJson.class);

        final SessionInfo sessionInfo;
        if (login.hasToken()) {
            sessionInfo = loginService.authenticateWithSessionId(login.getToken());
        } else if (login.hasUsername()) {
            sessionInfo = loginService.authenticateWithUsername(login.getUsername(), login.getPassword());
        } else {
            throw new AuthorizationScholagestException(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES,
                    "At least one of the token or username/password must be specified");
        }

        final SessionJson sessionJson = new SessionJson(sessionInfo.getToken(), sessionInfo.getUser().getId());
        return ResponseUtils.build200OkResponse(sessionJson);
    }
}
