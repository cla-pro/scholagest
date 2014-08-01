package net.scholagest.app.rest.ember;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/teacherFulls")
public class TeachersFullRest {

    @Path("/{id}")
    @GET
    public Teacher getTeacher(@PathParam("id") String id) {
        System.out.println("Request teacher full information with id=" + id);
        return TeachersRest.teachers.get(id);
    }
}
