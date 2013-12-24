package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;

import com.google.inject.Inject;

@Path("/teachers")
public class TeachersRest extends AbstractService {
    private static Map<String, Teacher> teachers = new HashMap<>();

    static {
        teachers.put("1", new Teacher("1", "Elodie", "Lavanchy"));
        teachers.put("2", new Teacher("2", "Thibaud", "Hottelier"));
        teachers.put("3", new Teacher("3", "Cédric", "Lavanchy"));
        teachers.put("4", new Teacher("4", "Valérie", "Parvex"));
    }

    @Inject
    public TeachersRest(ITeacherService teacherService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Teachers getTeachers(String param) {
        return new Teachers(new ArrayList<Teacher>(teachers.values()));
    }

    @Path("/{id}")
    @DELETE
    public void deleteTeacher(@PathParam("id") String id) {
        System.out.println("delete teacher with id=" + id);
        teachers.remove(id);
    }
}
