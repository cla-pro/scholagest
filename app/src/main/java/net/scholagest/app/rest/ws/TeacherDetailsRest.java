package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.TeacherJsonConverter;
import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.object.TeacherDetail;
import net.scholagest.service.TeacherServiceLocal;

import com.google.inject.Inject;

@Path("/teacherDetails")
public class TeacherDetailsRest {

    private final TeacherServiceLocal teacherService;

    @Inject
    public TeacherDetailsRest(final TeacherServiceLocal teacherService) {
        this.teacherService = teacherService;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getTeacherDetail(@PathParam("id") final String id) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherDetail teacherDetail = teacherService.getTeacherDetail(id);
        final TeacherDetailJson teacherDetailJson = converter.convertToTeacherDetailJson(teacherDetail);

        final Map<String, Object> response = new HashMap<>();
        response.put("teacherDetail", teacherDetailJson);

        return response;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveTeacherDetail(@PathParam("id") final String id, final Map<String, TeacherDetailJson> payload) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherDetailJson teacherDetailJson = payload.get("teacherDetail");
        final TeacherDetail teacherDetail = converter.convertToTeacherDetail(teacherDetailJson);

        teacherService.saveTeacherDetail(id, teacherDetail);
    }
}
