package net.scholagest.app.rest.ws;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public static Response buildOkResponse(final Object content) {
        final String json = new Gson().toJson(content);
        return Response.ok(json, MediaType.APPLICATION_JSON).build();
    }
}
