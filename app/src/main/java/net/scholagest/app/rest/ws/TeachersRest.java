package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.List;
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
import net.scholagest.app.rest.ws.converter.TeacherJsonConverter;
import net.scholagest.app.rest.ws.objects.TeacherDetailJson;
import net.scholagest.app.rest.ws.objects.TeacherJson;
import net.scholagest.object.Teacher;
import net.scholagest.service.TeacherServiceLocal;

import com.google.inject.Inject;

@Path("/teachers")
public class TeachersRest {
    private final TeacherServiceLocal teacherService;

    @Inject
    public TeachersRest(final TeacherServiceLocal teacherService) {
        this.teacherService = teacherService;
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getTeachers() {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final List<Teacher> teachers = teacherService.getTeachers();
        final List<TeacherJson> teachersJson = converter.convertToTeacherJson(teachers);

        final Map<String, Object> response = new HashMap<>();
        response.put("teachers", teachersJson);

        return response;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, TeacherJson> saveTeacher(@PathParam("id") final String id, final Map<String, TeacherJson> payload) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherJson teacherJson = payload.get("teacher");
        final Teacher teacher = converter.convertToTeacher(teacherJson);

        teacherService.saveTeacher(id, teacher);

        return payload;
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> createTeacher(final Map<String, TeacherJson> payload) {
        final TeacherJsonConverter converter = new TeacherJsonConverter();

        final TeacherJson receivedTeacherJson = payload.get("teacher");
        final Teacher receivedTeacher = converter.convertToTeacher(receivedTeacherJson);

        final Teacher createdTeacher = teacherService.createTeacher(receivedTeacher);
        final TeacherJson createdTeacherJson = converter.convertToTeacherJson(createdTeacher);
        final TeacherDetailJson createdTeacherDetailJson = converter.convertToTeacherDetailJson(createdTeacher.getDetail());

        final Map<String, Object> response = new HashMap<>();
        response.put("teacher", createdTeacherJson);
        response.put("teacherDetail", createdTeacherDetailJson);
        return response;
        // final TeacherJson teacher = payload.get("teacher");
        // final String id = IdHelper.getNextId(teachers.keySet());
        // teacher.setId(id);
        // teachers.put(id, teacher);
        //
        // final String detailId =
        // IdHelper.getNextId(TeacherDetailsRest.teacherDetails.keySet());
        // final TeacherDetailJson teacherDetail = new
        // TeacherDetailJson(detailId, null, null, null);
        // TeacherDetailsRest.teacherDetails.put(detailId, teacherDetail);
        //
        // final Map<String, Object> response = new HashMap<String, Object>();
        // response.put("teacher", teacher);
        // response.put("teacherDetail", teacherDetail);
        //
        // return response;
    }
}
