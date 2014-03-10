package net.scholagest.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.scholagest.exception.ScholagestRuntimeException;

/**
 * Indicate to the {@link AuthorizationInterceptor} that the roles and {@see
 * Permission}s must be checked before this method is actually invoked. If the
 * authorization fails, a {@link ScholagestRuntimeException} is thrown.
 * 
 * @author CLA
 * @since 0.8.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RolesAndPermissions {
    /**
     * Expected roles for this method to be executed
     */
    String[] roles();
}
