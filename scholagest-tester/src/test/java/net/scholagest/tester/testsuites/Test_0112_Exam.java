package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.creator.EntityContextSaver;
import net.scholagest.tester.utils.matcher.ListMatcher;
import net.scholagest.tester.utils.matcher.LongMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0112_Exam extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testPost() throws Exception {
        final Long branchPeriodId = entityContextSaver.getBranchPeriodEntity().getId();

        final ContentResponse response = callPOST("/services/exams", createExamJson("name", branchPeriodId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("exam", new JsonObject("name", "name", "branchPeriod", new LongMatcher(branchPeriodId)),
                "results", new ListMatcher(new JsonObject()), "studentResults", new ListMatcher(new JsonObject()), "branchPeriod", new JsonObject(
                        "id", new LongMatcher(branchPeriodId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long examId = entityContextSaver.getExamEntity().getId();
        final Long branchPeriodId = entityContextSaver.getBranchPeriodEntity().getId();

        final ContentResponse response = callPUT("/services/exams/" + examId, createExamJson("newName", branchPeriodId), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("exam", new JsonObject("id", new LongMatcher(examId), "name", "newName", "branchPeriod",
                new LongMatcher(branchPeriodId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private String createExamJson(final String name, final Long branchPeriodId) {
        return "{\"exam\": { \"name\": \"" + name + "\", \"coeff\": \"1\", " + "\"branchPeriod\": \"" + branchPeriodId + "\" }}";
    }
}
