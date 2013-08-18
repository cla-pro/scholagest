package net.scholagest.utils;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.AuthorizationRolesNamespace;

import org.apache.shiro.subject.Subject;
import org.mockito.Mockito;

public abstract class AbstractTest {
    public String getKeyspace() {
        return ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.KEYSPACE);
    }

    public void defineAdminSubject() {
        defineSubject(AuthorizationRolesNamespace.ROLE_ADMIN, false);
    }

    public void defineClassTeacherSubject() {
        defineSubject(AuthorizationRolesNamespace.ROLE_TEACHER, true);
    }

    public void defineClassHelpTeacherSubject() {
        defineSubject(AuthorizationRolesNamespace.ROLE_HELP_TEACHER, false);
    }

    public void defineOtherTeacherSubject() {
        defineSubject(AuthorizationRolesNamespace.ROLE_TEACHER, false);
    }

    private void defineSubject(String role, boolean isPermitted) {
        Subject subject = Mockito.mock(Subject.class);

        Mockito.when(subject.hasRole(role)).thenReturn(true);
        Mockito.when(subject.isPermitted(Mockito.anyString())).thenReturn(isPermitted);

        ScholagestThreadLocal.setSubject(subject);
    }

    public void assertMapEquals(Map<?, ?> mock, Map<?, ?> testee) {
        assertEquals(mock.size(), testee.size());

        for (Object key : mock.keySet()) {
            Object expected = mock.get(key);
            if (expected instanceof DBSet) {
                DBSet expectedDBSet = (DBSet) expected;
                DBSet actualDBSet = (DBSet) testee.get(key);
                assertEquals(expectedDBSet.size(), actualDBSet.size());
            } else {
                assertEquals(expected, testee.get(key));
            }
        }
    }
}
