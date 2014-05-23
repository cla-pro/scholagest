package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.EntityContextSaver;
import net.scholagest.tester.utils.matcher.ListMatcher;
import net.scholagest.tester.utils.matcher.LongMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0107_Year extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetAll() throws Exception {
        final Long yearId = entityContextSaver.getYearEntity().getId();

        final ContentResponse response = callGET("/services/years", new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("years", new ListMatcher(new JsonObject("id", yearId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long yearId = entityContextSaver.getYearEntity().getId();

        final ContentResponse response = callGET("/services/years/" + yearId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("year", new JsonObject("id", new LongMatcher(yearId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPost() throws Exception {
        final ContentResponse response = callPOST("/services/years", "{\"year\": { \"name\": \"name\" }}", token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("year", new JsonObject("name", "name"));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long yearId = entityContextSaver.getYearEntity().getId();

        final ContentResponse response = callPUT("/services/years/" + yearId, "{\"year\": { \"name\": \"newName\" }}", token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("year", new JsonObject("name", "newName"));
        jsonObject.assertEquals(response.getContentAsString());
    }
}
