package net.scholagest.app.rest.ember;

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

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.TeacherDetail;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;

import com.google.inject.Inject;

@Path("/teacherDetails")
public class TeacherDetailsRest extends AbstractService {
    public static Map<String, TeacherDetail> teacherDetails = new HashMap<>();

    static {
        teacherDetails.put("1", new TeacherDetail("1", "Kleefeldstrasse 1, 3018 Bern", "cedric.lavanchy@gmail.com", "+41791234567"));
        teacherDetails.put("2", new TeacherDetail("2", "Chemin des Merisiers 25, 1870 Monthey", "valerie.parvex@gmail.com", "+41797654321"));
    }

    @Inject
    public TeacherDetailsRest(final ITeacherService teacherService, final IOntologyService ontologyService, final IUserService userService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, TeacherDetail> getTeacherDetail(@PathParam("id") final String id) {
        final TeacherDetail teacherDetail = teacherDetails.get(id);

        final Map<String, TeacherDetail> result = new HashMap<>();
        result.put("teacherDetail", teacherDetail);

        return result;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveTeacherDetail(@PathParam("id") final String id, final Map<String, TeacherDetail> teacherDetail) {
        System.out.println("TeacherDetail must be saved " + id);
        mergeTeacherDetail(id, teacherDetail.get("teacherDetail"));
    }

    private void mergeTeacherDetail(final String id, final TeacherDetail teacherDetail) {
        final TeacherDetail toBeMerged = teacherDetails.get(id);

        toBeMerged.setAddress(teacherDetail.getAddress());
        toBeMerged.setEmail(teacherDetail.getEmail());
        toBeMerged.setPhone(teacherDetail.getPhone());
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, TeacherDetail> createTeacherDetail(final Map<String, TeacherDetail> payload) {
        final TeacherDetail teacherDetail = payload.get("teacherDetail");
        final String id = IdHelper.getNextId(teacherDetails.keySet());
        teacherDetail.setId(id);
        teacherDetails.put(id, teacherDetail);

        return payload;
    }
}
