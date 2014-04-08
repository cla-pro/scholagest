package net.scholagest.authorization;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicate to the {@link AuthorizationInterceptor} that this parameter's value
 * must be checked for permission. This annotation is useless if the
 * {@link RolesAndPermissions} has not been defined onto the method. If the
 * permission is not fulfilled, a ScholagestRuntimeException is thrown
 * 
 * @author CLA
 * @since 0.8.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface Permission {

}
