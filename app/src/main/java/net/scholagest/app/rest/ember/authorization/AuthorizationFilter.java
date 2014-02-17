package net.scholagest.app.rest.ember.authorization;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("Initializing filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        System.out.println("Entering filter with token: " + httpRequest.getHeader("Authorization"));
        chain.doFilter(request, response);
        System.out.println("Leaving filter");
    }

    @Override
    public void destroy() {
        System.out.println("Destroying filter");
    }

    // implements ContainerRequestFilter {
    // @Override
    // public ContainerRequest filter(ContainerRequest request) {
    // String authorization = request.getHeaderValue("Authorization");
    //
    // System.out.println("Authorization: " + authorization);
    //
    // return request;
    // }
}
