package net.scholagest.app.rest.ember;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Year;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/years")
public class YearsRest extends AbstractService {
    public static Map<String, Year> years = new HashMap<>();

    static {
        years.put("1", new Year("1", "2012-2013", false, Arrays.asList("1")));
        years.put("2", new Year("2", "2013-2014", true, Arrays.asList("2", "3")));
    }

    @Inject
    public YearsRest(final IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<Year>> getYears() {
        final Map<String, Collection<Year>> yearsToReturn = new HashMap<>();

        yearsToReturn.put("years", years.values());

        return yearsToReturn;
    }

    @CheckAuthorization
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public void createYear(final Map<String, Year> payload) {
        final Year year = payload.get("year");
        final String id = IdHelper.getNextId(years.keySet());
        year.setId(id);
        years.put(id, year);
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void saveYear(@PathParam("id") final String id, final Map<String, Year> payload) {
        final Year year = payload.get("year");
        mergeYear(years.get(id), year);
    }

    private void mergeYear(final Year base, final Year toMerge) {
        base.setName(toMerge.getName());
        base.setRunning(toMerge.isRunning());
        base.setClasses(toMerge.getClasses());
    }
}
