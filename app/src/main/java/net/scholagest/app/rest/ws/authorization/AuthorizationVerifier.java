package net.scholagest.app.rest.ws.authorization;

import javax.ws.rs.WebApplicationException;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AuthorizationVerifier implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Entering AuthorizationVerifier");
        // TODO verify token

        if (true) {
            return invocation.proceed();
        } else {
            throw new WebApplicationException(401);
        }
    }
}
