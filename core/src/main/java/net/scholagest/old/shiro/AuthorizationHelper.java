package net.scholagest.old.shiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.objects.PageObject;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;

import com.google.inject.Inject;

public class AuthorizationHelper {
    private IOntologyBusinessComponent ontologyBusinessComponent;

    @Inject
    public AuthorizationHelper(IOntologyBusinessComponent ontologyBusinessComponent) {
        this.ontologyBusinessComponent = ontologyBusinessComponent;
    }

    public void checkAuthorizationRoles(List<String> validRoles) throws ScholagestException {
        checkAuthorization(validRoles, Collections.<String> emptyList());
    }

    public void checkAuthorization(List<String> validRoles, List<String> validPermissions) throws ScholagestException {
        if (!isValid(validRoles, validPermissions)) {
            throw new ScholagestException(ScholagestExceptionErrorCode.INSUFFICIENT_PRIVILEGES, "Insufficient privilegies");
        }
    }

    private boolean isValid(List<String> validRoles, List<String> validPermissions) {
        Subject subject = ScholagestThreadLocal.getSubject();

        if (validRoles.isEmpty() && !checkPermissions(subject, validPermissions)) {
            return false;
        } else if (validPermissions.isEmpty() && !checkRoles(subject, validRoles)) {
            return false;
        } else if (!checkRoles(subject, validRoles) && !checkPermissions(subject, validPermissions)) {
            return false;
        }

        return true;
    }

    private boolean checkRoles(Subject subject, List<String> validRoles) {
        if (validRoles.isEmpty()) {
            return true;
        }

        for (String role : validRoles) {
            if (subject.hasRole(role)) {
                return true;
            }
        }

        return false;
    }

    private boolean checkPermissions(Subject subject, List<String> validPermissions) {
        if (validPermissions.isEmpty()) {
            return true;
        }

        for (String permission : validPermissions) {
            if (subject.isPermitted(permission)) {
                return true;
            }
        }

        return false;
    }

    public BaseObject filterObjectProperties(BaseObject object, List<String> validRoles, List<String> validPermissions) throws Exception {
        if (object == null) {
            return null;
        }

        boolean hasAllRight = isValid(validRoles, validPermissions);

        for (String propertyName : new ArrayList<String>(object.getProperties().keySet())) {
            OntologyElement propertyOntology = ontologyBusinessComponent.getElementWithName(propertyName);

            if (!hasAllRight && propertyOntology.getAttributeWithName(CoreNamespace.scholagestNs + "#restricted") != null) {
                object.removeProperty(propertyName);
            }
        }

        return object;
    }

    public Set<PageObject> filterPages(Set<PageObject> pageObjects) throws DatabaseException {
        Set<PageObject> filteredPages = new HashSet<>();
        Subject subject = ScholagestThreadLocal.getSubject();

        for (PageObject pageObject : pageObjects) {
            // System.out.println(pageObject.getPath() + " -> " +
            // pageObject.getRoles().values());
            if (checkRoles(subject, new ArrayList<>(pageObject.getRoles().values()))) {
                filteredPages.add(pageObject);
            }
        }

        return filteredPages;
    }
}
