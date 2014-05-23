package net.scholagest.app.rest.ws;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

/**
 * Class that provides useful methods to deal with {@link Response}
 * 
 * @author CLA
 * @since 0.17.0
 */
public final class ResponseUtils {
    /**
     * Build the 200 OK response with the given content.
     * 
     * @param content The response's content
     * @return The 200 OK response
     */
    public static Response build200OkResponse(final Object content) {
        final String json = new Gson().toJson(content);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }

    /**
     * Build the 401 Unauthorized response with a dummy realm
     * 
     * @return The 401 Unauthorized response
     */
    public static Response build401UnauthorizedResponse() {
        return Response.status(Status.UNAUTHORIZED).header("WWW-Authenticate", "Basic realm=\"test\"").build();
    }

    /**
     * Build the 401 Unauthorized response with a dummy realm
     * 
     * @return The 401 Unauthorized response
     */
    public static Response build500InternalServerErrorResponse(final Throwable throwable) {
        final PrintWriter printWriter = new PrintWriter(new StringWriter());
        throwable.printStackTrace(printWriter);
        printWriter.flush();
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(printWriter).build();
    }
}
