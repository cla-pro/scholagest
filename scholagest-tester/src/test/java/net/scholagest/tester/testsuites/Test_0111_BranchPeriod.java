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

public class Test_0111_BranchPeriod extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetMany() throws Exception {
        final Long branchPeriodId = entityContextSaver.getBranchPeriodEntity().getId();
        final Long branchId = entityContextSaver.getBranchEntity().getId();
        final Long examId = entityContextSaver.getExamEntity().getId();
        final Long studentResultId = entityContextSaver.getStudentResultEntity().getId();
        final Long resultId = entityContextSaver.getResultEntity().getId();
        final Long meanId = entityContextSaver.getMeanEntity().getId();

        final ContentResponse response = callGET("/services/branchPeriods", Arrays.asList(new UrlParameter("ids[]", "" + branchPeriodId)), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("branchPeriods", new ListMatcher(new JsonObject("id", new LongMatcher(branchPeriodId))),
                "branches", createListMatcherSingleObjectWithId(branchId), "exams", createListMatcherSingleObjectWithId(examId), "studentResults",
                createListMatcherSingleObjectWithId(studentResultId), "results", createListMatcherSingleObjectWithId(resultId), "means",
                createListMatcherSingleObjectWithId(meanId));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long branchPeriodId = entityContextSaver.getBranchPeriodEntity().getId();
        final Long branchId = entityContextSaver.getBranchEntity().getId();
        final Long examId = entityContextSaver.getExamEntity().getId();
        final Long studentResultId = entityContextSaver.getStudentResultEntity().getId();
        final Long resultId = entityContextSaver.getResultEntity().getId();
        final Long meanId = entityContextSaver.getMeanEntity().getId();

        final ContentResponse response = callGET("/services/branchPeriods/" + branchPeriodId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("branchPeriods", new ListMatcher(new JsonObject("id", new LongMatcher(branchPeriodId))),
                "branches", createListMatcherSingleObjectWithId(branchId), "exams", createListMatcherSingleObjectWithId(examId), "studentResults",
                createListMatcherSingleObjectWithId(studentResultId), "results", createListMatcherSingleObjectWithId(resultId), "means",
                createListMatcherSingleObjectWithId(meanId));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private ListMatcher createListMatcherSingleObjectWithId(final Long id) {
        return new ListMatcher(new JsonObject("id", new LongMatcher(id)));
    }
}
