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
import net.scholagest.tester.utils.matcher.LongMatcher;

import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

public class Test_0103_StudentMedical extends AbstractTestSuite {
    private String token;

    private final List<Long> studentMedicalIds = new ArrayList<>();

    @Before
    public void setUpData() {
        token = createAndGetSessionTokenForAdmin();

        final StudentEntity studentEntity1 = createAndPersistStudentEntity("Jean", "Aymar");
        final StudentEntity studentEntity2 = createAndPersistStudentEntity("Henry", "Gollay");
        final StudentEntity studentEntity3 = createAndPersistStudentEntity("Pascal", "Abordage");

        studentMedicalIds.add(studentEntity1.getStudentMedical().getId());
        studentMedicalIds.add(studentEntity2.getStudentMedical().getId());
        studentMedicalIds.add(studentEntity3.getStudentMedical().getId());
    }

    @Test
    public void testGetSingle() throws Exception {
        final Long studentMedicalId = studentMedicalIds.get(0);
        final ContentResponse response = callGET("/services/studentMedicals/" + studentMedicalId, new ArrayList<UrlParameter>(), token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("studentMedical", new JsonObject("id", new LongMatcher(studentMedicalId)));
        jsonObject.assertEquals(response.getContentAsString());
    }

    @Test
    public void testPut() throws Exception {
        final Long studentMedicalId = studentMedicalIds.get(0);
        final ContentResponse response = callPUT("/services/studentMedicals/" + studentMedicalId, "{\"studentMedical\": { \"doctor\": \"doctor\" }}",
                token);
        assertEquals(HttpStatus.OK_200, response.getStatus());

        final JsonObject jsonObject = new JsonObject("studentMedical", new JsonObject("doctor", "doctor"));
        jsonObject.assertEquals(response.getContentAsString());
    }

    private StudentEntity createAndPersistStudentEntity(final String firstname, final String lastname) {
        final StudentEntity studentEntity = StudentEntityCreator.createStudentEntity(firstname, lastname);
        persistInTransaction(studentEntity);

        final StudentPersonalEntity studentPersonalEntity = StudentEntityCreator.createStudentPersonalEntity(null, null, null, null, studentEntity);
        final StudentMedicalEntity studentMedicalEntity = StudentEntityCreator.createStudentMedicalEntity(null, studentEntity);

        persistInTransaction(studentPersonalEntity, studentMedicalEntity);
        studentEntity.setStudentPersonal(studentPersonalEntity);
        studentEntity.setStudentMedical(studentMedicalEntity);

        return studentEntity;
    }
}
