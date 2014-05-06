package net.scholagest.tester.testsuites;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.db.entity.StudentEntity;
import net.scholagest.db.entity.StudentMedicalEntity;
import net.scholagest.db.entity.StudentPersonalEntity;
import net.scholagest.tester.utils.AbstractTestSuite;
import net.scholagest.tester.utils.JsonObject;
import net.scholagest.tester.utils.UrlParameter;
import net.scholagest.tester.utils.creator.StudentEntityCreator;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0101_Student extends AbstractTestSuite {
    private String token;

    private final List<String> studentIds = new ArrayList<>();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();

        final StudentEntity studentEntity1 = createAndPersistStudentEntity("Jean", "Aymar");
        final StudentEntity studentEntity2 = createAndPersistStudentEntity("Henry", "Gollay");
        final StudentEntity studentEntity3 = createAndPersistStudentEntity("Pascal", "Abordage");

        studentIds.add("" + studentEntity1.getId());
        studentIds.add("" + studentEntity2.getId());
        studentIds.add("" + studentEntity3.getId());
    }

    @Test
    public void testGetAll() throws Exception {
        final ContentResponse response = callGET("/services/students", new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("students", new JsonObject());
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testGetMany() throws Exception {

    }

    @Test
    public void testGetSingle() throws Exception {
        final String studentId = studentIds.get(0);
        final ContentResponse response = callGET("/services/students/" + studentId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("student", new JsonObject("id", studentId));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPost() throws Exception {

    }

    @Test
    public void testPut() throws Exception {}

    private StudentEntity createAndPersistStudentEntity(final String firstname, final String lastname) {
        final StudentEntity studentEntity = StudentEntityCreator.createStudentEntity(firstname, lastname);
        persistInTransaction(studentEntity);

        final StudentPersonalEntity studentPersonalEntity = StudentEntityCreator.createStudentPersonalEntity(null, null, null, null, studentEntity);
        final StudentMedicalEntity studentMedicalEntity = StudentEntityCreator.createStudentMedicalEntity(null, studentEntity);

        persistInTransaction(studentPersonalEntity, studentMedicalEntity);

        return studentEntity;
    }
}
