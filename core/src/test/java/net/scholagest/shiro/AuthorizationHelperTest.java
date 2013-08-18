package net.scholagest.shiro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.exception.ScholagestException;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class AuthorizationHelperTest {
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private AuthorizationHelper testee;

    @Before
    public void setup() {
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        testee = new AuthorizationHelper(ontologyBusinessComponent);
    }

    @Test
    public void testCheckAuthorizationRoles() throws ScholagestException {
        Subject subject = Mockito.mock(Subject.class);
        Mockito.when(subject.hasRole(AuthorizationRolesNamespace.ROLE_TEACHER)).thenReturn(true);
        ScholagestThreadLocal.setSubject(subject);

        try {
            testee.checkAuthorizationRoles(Collections.<String> emptyList());
        } catch (ScholagestException e) {
            fail("Exception not expected");
        }

        try {
            testee.checkAuthorizationRoles(AuthorizationRolesNamespace.getAdminRole());
            fail("Exception expected");
        } catch (ScholagestException e) {
        }

        testee.checkAuthorizationRoles(AuthorizationRolesNamespace.getAllRoles());

        testee.checkAuthorizationRoles(Arrays.asList(AuthorizationRolesNamespace.ROLE_TEACHER));
    }

    @Test
    public void testCheckAuthorization() throws ScholagestException {
        String authorizedKey = UUID.randomUUID().toString();

        Subject subject = Mockito.mock(Subject.class);
        Mockito.when(subject.hasRole(AuthorizationRolesNamespace.ROLE_TEACHER)).thenReturn(true);
        Mockito.when(subject.isPermitted(authorizedKey)).thenReturn(true);
        ScholagestThreadLocal.setSubject(subject);

        try {
            testee.checkAuthorization(Collections.<String> emptyList(), Collections.<String> emptyList());
        } catch (ScholagestException e) {
            fail("Exception not expected");
        }

        try {
            testee.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Collections.<String> emptyList());
            fail("Exception expected");
        } catch (ScholagestException e) {
        }

        try {
            testee.checkAuthorization(Collections.<String> emptyList(), Arrays.asList(UUID.randomUUID().toString()));
            fail("Exception expected");
        } catch (ScholagestException e) {
        }

        try {
            testee.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(UUID.randomUUID().toString()));
            fail("Exception expected");
        } catch (ScholagestException e) {
        }

        testee.checkAuthorization(AuthorizationRolesNamespace.getAllRoles(), Collections.<String> emptyList());
        testee.checkAuthorization(Arrays.asList(AuthorizationRolesNamespace.ROLE_TEACHER), Collections.<String> emptyList());

        testee.checkAuthorization(Collections.<String> emptyList(), Arrays.asList(authorizedKey));
        testee.checkAuthorization(Collections.<String> emptyList(), Arrays.asList(UUID.randomUUID().toString(), authorizedKey));

        testee.checkAuthorization(AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(authorizedKey));
        testee.checkAuthorization(Arrays.asList(AuthorizationRolesNamespace.ROLE_TEACHER), Arrays.asList(authorizedKey));
    }

    @Test
    public void testFilterObjectProperties() throws Exception {
        String authorizedKey = UUID.randomUUID().toString();
        String restrictedProperty = "restricted";
        String openProperty = "open";

        Subject subject = Mockito.mock(Subject.class);
        Mockito.when(subject.hasRole(AuthorizationRolesNamespace.ROLE_TEACHER)).thenReturn(true);
        Mockito.when(subject.isPermitted(authorizedKey)).thenReturn(true);
        ScholagestThreadLocal.setSubject(subject);

        OntologyElement restrictedOntologyElement = createOntologyElement(restrictedProperty, true);
        OntologyElement openOntologyElement = createOntologyElement(openProperty, false);
        Mockito.when(ontologyBusinessComponent.getElementWithName(restrictedProperty)).thenReturn(restrictedOntologyElement);
        Mockito.when(ontologyBusinessComponent.getElementWithName(openProperty)).thenReturn(openOntologyElement);

        {
            BaseObject object = createBaseObject(Arrays.asList(restrictedProperty, openProperty));
            BaseObject filtered = testee.filterObjectProperties(object, AuthorizationRolesNamespace.getAdminRole(), Collections.<String> emptyList());

            assertNull(filtered.getProperty(restrictedProperty));
            assertEquals(openProperty, filtered.getProperty(openProperty));
        }

        {
            BaseObject object = createBaseObject(Arrays.asList(restrictedProperty, openProperty));
            BaseObject filtered = testee.filterObjectProperties(object, AuthorizationRolesNamespace.getAdminRole(), Arrays.asList(authorizedKey));

            assertEquals(restrictedProperty, filtered.getProperty(restrictedProperty));
            assertEquals(openProperty, filtered.getProperty(openProperty));
        }

        {
            BaseObject object = createBaseObject(Arrays.asList(restrictedProperty, openProperty));
            BaseObject filtered = testee.filterObjectProperties(object, Arrays.asList(AuthorizationRolesNamespace.ROLE_TEACHER),
                    Collections.<String> emptyList());

            assertEquals(restrictedProperty, filtered.getProperty(restrictedProperty));
            assertEquals(openProperty, filtered.getProperty(openProperty));
        }
    }

    @Test
    public void testFilterObjectPropertiesNullObject() throws Exception {
        BaseObject baseObject = null;
        assertNull(testee.filterObjectProperties(baseObject, Collections.<String> emptyList(), Collections.<String> emptyList()));
    }

    private BaseObject createBaseObject(List<String> properties) {
        BaseObject baseObject = new BaseObject(null, null);

        for (String prop : properties) {
            baseObject.putProperty(prop, prop);
        }

        return baseObject;
    }

    private OntologyElement createOntologyElement(String propertyName, boolean restricted) {
        OntologyElement ontologyElement = Mockito.mock(OntologyElement.class);

        if (restricted) {
            Mockito.when(ontologyElement.getAttributeWithName(CoreNamespace.scholagestNs + "#restricted")).thenReturn("restricted");
        }

        return ontologyElement;
    }
}
