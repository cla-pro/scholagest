package net.scholagest.app.rest.ember;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.StudentPersonal;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/studentPersonals")
public class StudentPersonalsRest extends AbstractService {
    public static Map<String, StudentPersonal> personals = new HashMap<>();

    static {
        personals.put("1", new StudentPersonal("1", "Route du Verney 8", "Perly", "1242", "Protestant"));
        personals.put("2", new StudentPersonal("2", "Post Street 711", "San Francisco", "1242", null));
    }

    @Inject
    public StudentPersonalsRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentPersonal> getStudentPersonal(@PathParam("id") final String id) {
        final StudentPersonal studentPersonal = personals.get(id);

        final Map<String, StudentPersonal> result = new HashMap<>();
        result.put("studentPersonal", studentPersonal);

        return result;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    public void saveStudentPersonal(@PathParam("id") final String id, final Map<String, StudentPersonal> payload) {
        final StudentPersonal studentPersonal = payload.get("studentPersonal");
        mergeStudentPersonal(personals.get(id), studentPersonal);
    }

    private void mergeStudentPersonal(StudentPersonal base, StudentPersonal toMerge) {
        base.setCity(toMerge.getCity());
        base.setPostcode(toMerge.getPostcode());
        base.setReligion(toMerge.getReligion());
        base.setStreet(toMerge.getStreet());
    }
}
