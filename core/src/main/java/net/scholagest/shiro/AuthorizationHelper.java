package net.scholagest.shiro;

import java.util.Collections;
import java.util.List;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;

public class AuthorizationHelper {
    public void checkAuthorizationRoles(List<String> validRoles) throws ScholagestException {
        checkAuthorization(validRoles, Collections.<String> emptyList());
    }

    public void checkAuthorization(List<String> validRoles, List<String> validPermissions) throws ScholagestException {
        Subject subject = ScholagestThreadLocal.getSubject();

        if (!checkRoles(subject, validRoles) && !checkPermissions(subject, validPermissions)) {
            throw new ScholagestException(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, "Insufficient privilegies");
        }
    }

    private boolean checkRoles(Subject subject, List<String> validRoles) {
        for (String role : validRoles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPermissions(Subject subject, List<String> validPermissions) {
        for (String permission : validPermissions) {
            if (subject.isPermitted(permission)) {
                return true;
            }
        }

        return false;
    }

    public BaseObject filterObjectProperties(BaseObject object) {
        // TODO implement me
        return object;
    }
}
