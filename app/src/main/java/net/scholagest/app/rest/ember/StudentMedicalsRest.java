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
import net.scholagest.app.rest.ember.objects.StudentMedical;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/studentMedicals")
public class StudentMedicalsRest extends AbstractService {
    public static Map<String, StudentMedical> medicals = new HashMap<>();

    static {
        medicals.put("1", new StudentMedical("1", null));
        medicals.put("2", new StudentMedical("2", null));
    }

    @Inject
    public StudentMedicalsRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentMedical> getMedical(@PathParam("id") String id) {
        StudentMedical studentMedical = medicals.get(id);

        final Map<String, StudentMedical> result = new HashMap<>();
        result.put("studentMedical", studentMedical);

        return result;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    public void saveStudentPersonal(@PathParam("id") final String id, final Map<String, StudentMedical> payload) {
        final StudentMedical studentMedical = payload.get("studentMedical");
        mergeStudentMedical(medicals.get(id), studentMedical);
    }

    private void mergeStudentMedical(StudentMedical base, StudentMedical toMerge) {
        base.setDoctor(toMerge.getDoctor());
        ;
    }
}
