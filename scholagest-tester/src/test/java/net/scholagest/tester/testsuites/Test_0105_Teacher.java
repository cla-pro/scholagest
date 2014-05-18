package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.scholagest.db.entity.TeacherDetailEntity;
import net.scholagest.db.entity.TeacherEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.TeacherEntityCreator;
import net.scholagest.tester.utils.matcher.EqMatcher;
import net.scholagest.tester.utils.matcher.ListMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0105_Teacher extends AbstractTestSuite {
    private String token;

    private final List<Long> teacherIds = new ArrayList<>();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();

        final TeacherEntity teacherEntity1 = createAndPersistTeacherEntity("Jean", "Aymar");
        final TeacherEntity teacherEntity2 = createAndPersistTeacherEntity("Henry", "Gollay");
        final TeacherEntity teacherEntity3 = createAndPersistTeacherEntity("Pascal", "Abordage");

        teacherIds.add(teacherEntity1.getId());
        teacherIds.add(teacherEntity2.getId());
        teacherIds.add(teacherEntity3.getId());
    }

    @Test
    public void testGetAll() throws Exception {
        final ContentResponse response = callGET("/services/teachers", new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("teachers", new ListMatcher(new JsonObject(), new JsonObject("id", teacherIds.get(0)),
                new JsonObject("id", teacherIds.get(1)), new JsonObject("id", teacherIds.get(2))));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetMany() throws Exception {
        final ContentResponse response = callGET("/services/teachers",
                Arrays.asList(new UrlParameter("ids[]", "" + teacherIds.get(1)), new UrlParameter("ids[]", "" + teacherIds.get(2))), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("teachers", new ListMatcher(new JsonObject("id", teacherIds.get(1)), new JsonObject("id",
                teacherIds.get(2))));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPost() throws Exception {
        final ContentResponse response = callPOST("/services/teachers",
                "{\"teacher\": { \"firstname\": \"firstname\", \"lastname\": \"lastname\" }}", token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("teacher", new JsonObject(), "teacherDetail", new JsonObject());
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long teacherId = teacherIds.get(0);
        final ContentResponse response = callPUT("/services/teachers/" + teacherId,
                "{\"teacher\": { \"firstname\": \"firstname\", \"lastname\": \"lastname\" }}", token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("teacher", new JsonObject("firstname", new EqMatcher("firstname"), "lastname", new EqMatcher(
                "lastname")));
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
