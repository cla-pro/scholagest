package net.scholagest.services.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import net.scholagest.business.IOntologyBusinessComponent;
import net.scholagest.business.IPageBusinessComponent;
import net.scholagest.business.IUserBusinessComponent;
import net.scholagest.database.DatabaseException;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.AuthorizationRolesNamespace;
import net.scholagest.objects.PageObject;
import net.scholagest.services.IUserService;
import net.scholagest.utils.AbstractTest;
import net.scholagest.utils.InMemoryDatabase;

import org.apache.shiro.ShiroException;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserServiceTest extends AbstractTest {
    private InMemoryDatabase database;

    private IUserBusinessComponent userBusinessComponent;
    private IPageBusinessComponent pageBusinessComponent;
    private IOntologyBusinessComponent ontologyBusinessComponent;

    private IUserService testee;

    @Before
    public void setup() {
        database = Mockito.spy(new InMemoryDatabase());
        defineAdminSubject();

        userBusinessComponent = Mockito.mock(IUserBusinessComponent.class);
        pageBusinessComponent = Mockito.mock(IPageBusinessComponent.class);
        ontologyBusinessComponent = Mockito.mock(IOntologyBusinessComponent.class);

        testee = new UserService(database, userBusinessComponent, pageBusinessComponent, ontologyBusinessComponent);
    }

    @Test
    public void testGetVisibleModules() throws DatabaseException, Exception {
        Set<PageObject> pageSet = createPageSet();
        Mockito.when(pageBusinessComponent.getAllPages()).thenReturn(pageSet);

        List<String> visibleModules = testee.getVisibleModules(UUID.randomUUID().toString());

        assertEquals(1, visibleModules.size());
        assertEquals(pageSet.iterator().next().getPath(), visibleModules.get(0));
    }

    @Test
    public void testGetVisibleModulesPageFiltered() throws DatabaseException, Exception {
        defineClassTeacherSubject();
        Set<PageObject> pageSet = createPageSet();
        Mockito.when(pageBusinessComponent.getAllPages()).thenReturn(pageSet);

        List<String> visibleModules = testee.getVisibleModules(UUID.randomUUID().toString());

        assertTrue(visibleModules.isEmpty());
    }

    private Set<PageObject> createPageSet() throws DatabaseException {
        Set<PageObject> pageObjectSet = new HashSet<>();

        pageObjectSet.add(createPageObject());

        return pageObjectSet;
    }

    private PageObject createPageObject() throws DatabaseException {
        PageObject pageObject = new PageObject(UUID.randomUUID().toString());

        DBSet roles = new DBSet(database.getTransaction(getKeyspace()), UUID.randomUUID().toString());
        roles.add(AuthorizationRolesNamespace.ROLE_ADMIN);
        pageObject.setRoles(roles);
        pageObject.setPath("path");

        pageObject.flushAllProperties();

        return pageObject;
    }

    @Test
    public void testAuthenticateWithUsername() throws Exception {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Subject subject = Mockito.mock(Subject.class);

        Mockito.when(userBusinessComponent.authenticateUser(username, password)).thenReturn(subject);

        Subject result = testee.authenticateWithUsername(username, password);

        assertEquals(subject, result);
        Mockito.verify(userBusinessComponent).authenticateUser(username, password);
    }

    @Test(expected = ShiroException.class)
    public void testAuthenticateWithUsernameWithException() throws Exception {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();

        Mockito.when(userBusinessComponent.authenticateUser(username, password)).thenThrow(new ShiroException());

        testee.authenticateWithUsername(username, password);
        fail("Exception expected");
    }

    @Test
    public void testAuthenticateWithToken() throws Exception {
        String token = UUID.randomUUID().toString();
        Subject subject = Mockito.mock(Subject.class);

        Mockito.when(userBusinessComponent.authenticateToken(token)).thenReturn(subject);

        Subject result = testee.authenticateWithToken(token);

        assertEquals(subject, result);
        Mockito.verify(userBusinessComponent).authenticateToken(token);
    }

    @Test(expected = ShiroException.class)
    public void testAuthenticateWithTokenWithException() throws Exception {
        String token = UUID.randomUUID().toString();

        Mockito.when(userBusinessComponent.authenticateToken(token)).thenThrow(new ShiroException());

        testee.authenticateWithToken(token);
        fail("Exception expected");
    }
}
