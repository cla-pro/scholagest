package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

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

public class Test_0108_Clazz extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetMany() throws Exception {
        final Long clazzId = entityContextSaver.getClazzEntity().getId();

        final ContentResponse response = callGET("/services/classes", Arrays.asList(new UrlParameter("ids[]", "" + clazzId)), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("classes", new ListMatcher(new JsonObject("id", clazzId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long clazzId = entityContextSaver.getClazzEntity().getId();

        final ContentResponse response = callGET("/services/classes/" + clazzId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("class", new JsonObject("id", new LongMatcher(clazzId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPost() throws Exception {
        final Long yearId = entityContextSaver.getYearEntity().getId();

        final ContentResponse response = callPOST("/services/classes", createClazzJson("name", yearId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("class", new JsonObject("name", "name"));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long yearId = entityContextSaver.getYearEntity().getId();
        final Long clazzId = entityContextSaver.getClazzEntity().getId();

        final ContentResponse response = callPUT("/services/classes/" + clazzId, createClazzJson("newName", yearId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("class", new JsonObject("name", "newName", "year", new LongMatcher(yearId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private String createClazzJson(final String name, final Long yearId) {
        return "{\"class\": { \"name\": \"" + name + "\", \"periods\": [], " + "\"branches\": [], \"students\": [], \"teachers\": [], \"year\": \""
                + yearId + "\" }}";
    }
}
