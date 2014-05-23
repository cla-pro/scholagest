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

public class Test_0109_Period extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetMany() throws Exception {
        final Long periodId = entityContextSaver.getPeriodEntity().getId();

        final ContentResponse response = callGET("/services/periods", Arrays.asList(new UrlParameter("ids[]", "" + periodId)), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("periods", new ListMatcher(new JsonObject("id", periodId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long periodId = entityContextSaver.getPeriodEntity().getId();

        final ContentResponse response = callGET("/services/periods/" + periodId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("period", new JsonObject("id", new LongMatcher(periodId)));
        jsonObject.assertEquals(response.getContentAsString());
    }
}
