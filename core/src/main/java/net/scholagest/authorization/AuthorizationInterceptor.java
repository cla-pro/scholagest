package net.scholagest.authorization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.utils.ScholagestThreadLocal;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AuthorizationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        // 1. Get the roles and permissions to check
        // 2. Check the permissions
        // 3. Either execute the method or throw an exception (405 not allowed)
        final RolesAndPermissions rolesAndPermissions = invocation.getMethod().getAnnotation(RolesAndPermissions.class);

        if (checkRolesAndPermissions(rolesAndPermissions, invocation.getMethod(), invocation.getArguments())) {
            return invocation.proceed();
        } else {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, "Insufficient privilegies to run the method");
        }
    }

    private boolean checkRolesAndPermissions(final RolesAndPermissions rolesAndPermissions, final Method method, final Object[] arguments) {
        if (ScholagestThreadLocal.getSubject() == null) {
            return false;
        } else if (isNothingRequired(rolesAndPermissions, method)) {
            return true;
        } else if (isArraySetAndNotEmpty(rolesAndPermissions.roles()) && hasRoles(rolesAndPermissions.roles())) {
            return true;
        } else if (hasPermissions(method) && isPermitted(method, arguments)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isNothingRequired(final RolesAndPermissions rolesAndPermissions, final Method method) {
        final boolean requiresRoles = isArraySetAndNotEmpty(rolesAndPermissions.roles());
        final boolean requiresPermission = hasPermissions(method);
        return !requiresRoles && !requiresPermission;
    }

    private boolean hasPermissions(final Method method) {
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (final Annotation[] annotations : parameterAnnotations) {
            for (final Annotation annotation : annotations) {
                if (annotation.annotationType() == Permission.class) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean hasRoles(final String[] roles) {
        for (final String role : roles) {
            if (ScholagestThreadLocal.getSubject().hasRole(role)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPermitted(final Method method, final Object[] arguments) {
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            final Annotation[] annotations = parameterAnnotations[i];
            if (hasParameterPermission(annotations) && isParameterPermitted(arguments[i])) {
                return true;
            }
        }

        return false;
    }

    private boolean hasParameterPermission(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType().equals(Permission.class)) {
                return true;
            }
        }
        return false;
    }

    private boolean isParameterPermitted(final Object argument) {
        if (!(argument instanceof String)) {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.GENERAL, "The checkPermission applies only on String");
        }
        return ScholagestThreadLocal.getSubject().isPermitted((String) argument);
    }

    private boolean isArraySetAndNotEmpty(final String[] array) {
        return array != null && array.length > 0;
    }
}
