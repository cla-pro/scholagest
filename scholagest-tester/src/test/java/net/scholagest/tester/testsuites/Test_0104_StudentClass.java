package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.EntityContextSaver;
import net.scholagest.tester.utils.matcher.ListMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0104_StudentClass extends AbstractTestSuite {
    private String token;

    private final EntityContextSaver entityContextSaver = new EntityContextSaver();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();
        entityContextSaver.createAndPersistEntityContext(getTransactionalHelper());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long studentId = entityContextSaver.getStudentEntity().getId();

        final ContentResponse response = callGET("/services/studentClasses/" + studentId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("studentClass", new JsonObject("currentClasses", new ListMatcher(), "oldClasses",
                new ListMatcher()));
        jsonObject.assertEquals(response.getContentAsString());
    }
}
