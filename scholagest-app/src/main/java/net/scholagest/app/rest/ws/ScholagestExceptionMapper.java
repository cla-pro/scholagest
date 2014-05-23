package net.scholagest.app.rest.ws;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.scholagest.exception.AuthorizationScholagestException;

/**
 * Mapper that transforms the {@link AuthorizationScholagestException} into the 401 Unauthorized HTTP response
 * 
 * @author CLA
 * @since 0.17.0
 */
@Provider
public class ScholagestExceptionMapper implements ExceptionMapper<AuthorizationScholagestException> {
    /**
     * {@inheritDoc}
     */
    @Override
    public Response toResponse(final AuthorizationScholagestException exception) {
        return ResponseUtils.build401UnauthorizedResponse();
    }
}
