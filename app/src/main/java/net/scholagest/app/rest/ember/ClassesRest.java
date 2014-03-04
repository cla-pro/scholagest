package net.scholagest.app.rest.ember;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Clazz;
import net.scholagest.app.rest.ember.objects.Period;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/classes")
public class ClassesRest extends AbstractService {
    public static Map<String, Clazz> classes = new HashMap<>();

    static {
        classes.put("1", new Clazz("1", "1P A", "1", Arrays.asList("1", "2", "3"), Arrays.asList("1"), Arrays.asList("1"), Arrays.asList("1", "2")));
        classes.put("2", new Clazz("2", "2P A", "2", new ArrayList<String>(), Arrays.asList("2"), Arrays.asList("2"), new ArrayList<String>()));
        classes.put("3", new Clazz("3", "5P A", "2", new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
                new ArrayList<String>()));
    }

    @Inject
    public ClassesRest(final IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, List<Clazz>> getClasses(@QueryParam("ids[]") final List<String> ids) {
        final Map<String, List<Clazz>> toReturn = new HashMap<>();
        final List<Clazz> classesToReturn = new ArrayList<>();

        for (final String id : ids) {
            classesToReturn.add(classes.get(id));
        }
        toReturn.put("classes", classesToReturn);

        return toReturn;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Clazz> getClass(@PathParam("id") final String id) {
        final Map<String, Clazz> toReturn = new HashMap<>();

        final Clazz clazz = classes.get(id);
        toReturn.put("class", clazz);

        return toReturn;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    public void saveClass(@PathParam("id") final String id, final Map<String, Clazz> payload) {
        final Clazz clazz = payload.get("class");
        mergeClazz(classes.get(id), clazz);
    }

    private void mergeClazz(final Clazz base, final Clazz toMerge) {
        base.setName(toMerge.getName());
        base.setStudents(toMerge.getStudents());
        base.setTeachers(toMerge.getTeachers());
    }

    @CheckAuthorization
    @POST
    public Map<String, Object> createClass(final Map<String, Clazz> payload) {
        final Clazz clazz = payload.get("class");

        final String id = IdHelper.getNextId(classes.keySet());
        clazz.setId(id);
        classes.put(id, clazz);

        final List<Period> periods = createPeriods(clazz);

        final Map<String, Object> response = new HashMap<>();
        response.put("class", clazz);
        response.put("periods", periods);

        return response;
    }

    private List<Period> createPeriods(final Clazz clazz) {
        final List<Period> periods = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            final String periodId = IdHelper.getNextId(PeriodsRest.periods.keySet());
            final Period period = new Period(periodId, "Trimestre " + (i + 1), clazz.getId(), new ArrayList<String>());
            PeriodsRest.periods.put(periodId, period);
            periods.add(period);
            clazz.getPeriods().add(periodId);
        }

        return periods;
    }
}
