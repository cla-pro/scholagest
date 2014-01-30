package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.objects.Clazz;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/classes")
public class ClassesRest extends AbstractService {
    public static Map<String, Clazz> classes = new HashMap<>();

    static {
        classes.put("1", new Clazz("1", "1P A", Arrays.asList("1", "2", "3"), Arrays.asList("1"), Arrays.asList("1")));
        classes.put("2", new Clazz("2", "2P A", new ArrayList<String>(), Arrays.asList("2"), Arrays.asList("2")));
        classes.put("3", new Clazz("3", "5P A", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>()));
    }

    @Inject
    public ClassesRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Clazz>> getClasses(@QueryParam("ids[]") final List<String> ids) {
        final Map<String, List<Clazz>> toReturn = new HashMap<>();
        final List<Clazz> classesToReturn = new ArrayList<>();

        for (String id : ids) {
            classesToReturn.add(classes.get(id));
        }
        toReturn.put("classes", classesToReturn);

        return toReturn;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Clazz> getClass(@PathParam("id") String id) {
        final Map<String, Clazz> toReturn = new HashMap<>();

        Clazz clazz = classes.get(id);
        toReturn.put("class", clazz);

        return toReturn;
    }
}
