package net.scholagest.app.rest.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.Teacher;
import net.scholagest.app.rest.ws.objects.TeacherDetail;
import net.scholagest.app.rest.ws.objects.Teachers;

import com.google.inject.Inject;

@Path("/teachers")
public class TeachersRest {
    public static Map<String, Teacher> teachers = new HashMap<>();

    static {
        teachers.put("1", new Teacher("1", "Cédric", "Lavanchy", "1"));
        teachers.put("2", new Teacher("2", "Valérie", "Parvex", "2"));
    }

    @Inject
    public TeachersRest() {}

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Teachers getTeachers() {
        return new Teachers(new ArrayList<Teacher>(teachers.values()));
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveTeacher(@PathParam("id") final String id, final Map<String, Teacher> teacher) {
        mergeTeachers(id, teacher.get("teacher"));
    }

    private void mergeTeachers(final String id, final Teacher teacher) {
        final Teacher toBeMerged = teachers.get(id);

        toBeMerged.setFirstName(teacher.getFirstName());
        toBeMerged.setLastName(teacher.getLastName());
    }

    @CheckAuthorization
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> createTeacher(final Map<String, Teacher> payload) {
        final Teacher teacher = payload.get("teacher");
        final String id = IdHelper.getNextId(teachers.keySet());
        teacher.setId(id);
        teachers.put(id, teacher);

        final String detailId = IdHelper.getNextId(TeacherDetailsRest.teacherDetails.keySet());
        final TeacherDetail teacherDetail = new TeacherDetail(detailId, null, null, null);
        TeacherDetailsRest.teacherDetails.put(detailId, teacherDetail);

        final Map<String, Object> response = new HashMap<String, Object>();
        response.put("teacher", teacher);
        response.put("teacherDetail", teacherDetail);

        return response;
    }

    @CheckAuthorization
    @Path("/{id}")
    @DELETE
    public void deleteTeacher(@PathParam("id") final String id) {
        teachers.remove(id);
    }
}
