package net.scholagest.app.rest.ember;

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

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.objects.Teacher;
import net.scholagest.app.rest.ember.objects.Teachers;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;

import com.google.inject.Inject;

@Path("/teachers")
public class TeachersRest extends AbstractService {
    public static Map<String, Teacher> teachers = new HashMap<>();

    static {
        teachers.put("1", new Teacher("1", "Cédric", "Lavanchy", "1"));
        teachers.put("2", new Teacher("2", "Valérie", "Parvex", "2"));
    }

    @Inject
    public TeachersRest(ITeacherService teacherService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Teachers getTeachers() {
        return new Teachers(new ArrayList<Teacher>(teachers.values()));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveTeacher(@PathParam("id") String id, Map<String, Teacher> teacher) {
        System.out.println("Teacher must be saved " + id);
        mergeTeachers(id, teacher.get("teacher"));
    }

    private void mergeTeachers(String id, Teacher teacher) {
        Teacher toBeMerged = teachers.get(id);

        toBeMerged.setFirstName(teacher.getFirstName());
        toBeMerged.setLastName(teacher.getLastName());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Teacher> createTeacher(Map<String, Teacher> payload) {
        Teacher teacher = payload.get("teacher");
        String id = getNextId(teachers);
        teacher.setId(id);
        teachers.put(id, teacher);

        return payload;
    }

    private String getNextId(Map<String, Teacher> teachers) {
        int max = 0;
        for (String id : teachers.keySet()) {
            Integer intId = Integer.valueOf(id);
            if (intId > max) {
                max = intId;
            }
        }

        return "" + (max + 1);
    }

    @Path("/{id}")
    @DELETE
    public void deleteTeacher(@PathParam("id") String id) {
        System.out.println("delete teacher with id=" + id);
        teachers.remove(id);
    }
}
