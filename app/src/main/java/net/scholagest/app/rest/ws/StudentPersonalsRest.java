package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.StudentJsonConverter;
import net.scholagest.app.rest.ws.objects.StudentPersonalJson;
import net.scholagest.object.StudentPersonal;
import net.scholagest.service.StudentServiceLocal;

import com.google.inject.Inject;

@Path("/studentPersonals")
public class StudentPersonalsRest {
    // public static Map<String, StudentPersonalJson> personals = new
    // HashMap<>();
    //
    // static {
    // personals.put("1", new StudentPersonalJson("1", "Route du Verney 8",
    // "Perly", "1242", "Protestant"));
    // personals.put("2", new StudentPersonalJson("2", "Post Street 711",
    // "San Francisco", "1242", null));
    // }

    private final StudentServiceLocal studentService;

    @Inject
    public StudentPersonalsRest(final StudentServiceLocal studentService) {
        this.studentService = studentService;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentPersonalJson> getStudentPersonal(@PathParam("id") final String id) {
        final StudentPersonal studentPersonal = studentService.getStudentPersonal(id);
        final StudentPersonalJson studentPersonalJson = new StudentJsonConverter().convertToStudentPersonalJson(studentPersonal);

        final Map<String, StudentPersonalJson> result = new HashMap<>();
        result.put("studentPersonal", studentPersonalJson);

        return result;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentPersonalJson> saveStudentPersonal(@PathParam("id") final String id, final Map<String, StudentPersonalJson> payload) {
        final StudentJsonConverter converter = new StudentJsonConverter();

        final StudentPersonalJson studentPersonalJson = payload.get("studentPersonal");
        final StudentPersonal studentPersonal = converter.convertToStudentPersonal(studentPersonalJson);

        final StudentPersonal updated = studentService.saveStudentPersonal(id, studentPersonal);
        final StudentPersonalJson updatedJson = converter.convertToStudentPersonalJson(updated);

        final Map<String, StudentPersonalJson> result = new HashMap<>();
        result.put("studentPersonal", updatedJson);

        return result;
    }
}
