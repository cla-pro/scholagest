package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.TeacherEntityCreator;
import net.scholagest.tester.utils.matcher.LongMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0106_TeacherDetail extends AbstractTestSuite {
    private String token;

    private final List<Long> teacherDetailIds = new ArrayList<>();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();

        final TeacherEntity teacherEntity1 = createAndPersistTeacherEntity("Jean", "Aymar");
        final TeacherEntity teacherEntity2 = createAndPersistTeacherEntity("Henry", "Gollay");
        final TeacherEntity teacherEntity3 = createAndPersistTeacherEntity("Pascal", "Abordage");

        teacherDetailIds.add(teacherEntity1.getTeacherDetail().getId());
        teacherDetailIds.add(teacherEntity2.getTeacherDetail().getId());
        teacherDetailIds.add(teacherEntity3.getTeacherDetail().getId());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long teacherDetailId = teacherDetailIds.get(0);
        final ContentResponse response = callGET("/services/teacherDetails/" + teacherDetailId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("teacherDetail", new JsonObject("id", new LongMatcher(teacherDetailId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long teacherDetailId = teacherDetailIds.get(0);
        final ContentResponse response = callPUT("/services/teacherDetails/" + teacherDetailId,
                "{\"teacherDetail\": { \"address\": \"address\", \"email\": \"email\" }}", token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("teacherDetail", new JsonObject("address", "address", "email", "email"));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private TeacherEntity createAndPersistTeacherEntity(final String firstname, final String lastname) {
        final TeacherEntity teacherEntity = TeacherEntityCreator.createTeacherEntity(firstname, lastname, null);
        persistInTransaction(teacherEntity);

        final TeacherDetailEntity teacherDetailEntity = TeacherEntityCreator.createTeacherDetailEntity(null, null, null, teacherEntity);

        persistInTransaction(teacherDetailEntity);
        teacherEntity.setTeacherDetail(teacherDetailEntity);

        return teacherEntity;
    }
}
