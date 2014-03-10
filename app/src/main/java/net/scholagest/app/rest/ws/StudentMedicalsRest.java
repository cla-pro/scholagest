package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.StudentMedical;

import com.google.inject.Inject;

@Path("/studentMedicals")
public class StudentMedicalsRest {
    public static Map<String, StudentMedical> medicals = new HashMap<>();

    static {
        medicals.put("1", new StudentMedical("1", null));
        medicals.put("2", new StudentMedical("2", null));
    }

    @Inject
    public StudentMedicalsRest() {}

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, StudentMedical> getMedical(@PathParam("id") final String id) {
        final StudentMedical studentMedical = medicals.get(id);

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

    private void mergeStudentMedical(final StudentMedical base, final StudentMedical toMerge) {
        base.setDoctor(toMerge.getDoctor());
        ;
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, StudentMedical> createStudentPersonals(final Map<String, StudentMedical> payload) {
        final StudentMedical studentMedical = payload.get("studentMedical");
        final String id = IdHelper.getNextId(medicals.keySet());
        studentMedical.setId(id);
        medicals.put(id, studentMedical);

        return payload;
    }
}
