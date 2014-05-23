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

public class Test_0110_Branch extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetMany() throws Exception {
        final Long branchId = entityContextSaver.getBranchEntity().getId();

        final ContentResponse response = callGET("/services/branches", Arrays.asList(new UrlParameter("ids[]", "" + branchId)), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("branches", new ListMatcher(new JsonObject("id", branchId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long branchId = entityContextSaver.getBranchEntity().getId();

        final ContentResponse response = callGET("/services/branches/" + branchId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("branch", new JsonObject("id", new LongMatcher(branchId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPost() throws Exception {
        final Long clazzId = entityContextSaver.getClazzEntity().getId();
        final Long periodId = entityContextSaver.getPeriodEntity().getId();

        final ContentResponse response = callPOST("/services/branches", createBranchJson("name", clazzId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("branch", new JsonObject("name", "name"), "branchPeriods", new ListMatcher(new JsonObject(
                "period", new LongMatcher(periodId))), "periods", new ListMatcher(new JsonObject("id", new LongMatcher(periodId))));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long branchId = entityContextSaver.getBranchEntity().getId();
        final Long clazzId = entityContextSaver.getClazzEntity().getId();

        final ContentResponse response = callPUT("/services/branches/" + branchId, createBranchJson("newName", clazzId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("branch", new JsonObject("id", new LongMatcher(branchId), "name", "newName", "clazz",
                new LongMatcher(clazzId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private String createBranchJson(final String name, final Long clazzId) {
        return "{\"branch\": { \"name\": \"" + name + "\", \"numerical\": \"false\", " + "\"branchPeriods\": [], \"clazz\": \"" + clazzId + "\" }}";
    }
}
