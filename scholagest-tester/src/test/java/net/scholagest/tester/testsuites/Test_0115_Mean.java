package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.EntityContextSaver;
import net.scholagest.tester.utils.matcher.DoubleMatcher;
import net.scholagest.tester.utils.matcher.LongMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0115_Mean extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long meanId = entityContextSaver.getMeanEntity().getId();

        final ContentResponse response = callGET("/services/means/" + meanId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("mean", new JsonObject("id", new LongMatcher(meanId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long meanId = entityContextSaver.getMeanEntity().getId();
        final Long studentResultId = entityContextSaver.getStudentResultEntity().getId();

        final ContentResponse response = callPUT("/services/means/" + meanId, createMeanJson("6.0", studentResultId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("mean", new JsonObject("id", new LongMatcher(meanId), "grade", new DoubleMatcher(6.0),
                "studentResult", new LongMatcher(studentResultId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private String createMeanJson(final String grade, final Long studentResultId) {
        return "{\"mean\": { \"grade\": \"" + grade + "\", \"studentResult\": \"" + studentResultId + "\" }}";
    }
}
