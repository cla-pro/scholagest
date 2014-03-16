package net.scholagest.app.rest.ws.authorization;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import net.scholagest.utils.ScholagestThreadLocal;

/**
 * Filter used to extract the authentication's token from the request header.
 * 
 * @author CLA
 * @since 0.12.0
 */
public class AuthorizationFilter implements Filter {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        final String sessionId = httpRequest.getHeader("Authorization");
        ScholagestThreadLocal.setSessionId(sessionId);

        try {
            chain.doFilter(request, response);
        } finally {
            ScholagestThreadLocal.setSessionId(null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        // Do nothing
    }
}
