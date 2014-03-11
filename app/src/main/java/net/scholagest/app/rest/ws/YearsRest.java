package net.scholagest.app.rest.ws;

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

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.Year;

import com.google.inject.Inject;

@Path("/years")
public class YearsRest {
    public static Map<String, Year> years = new HashMap<>();

    static {
        years.put("1", new Year("1", "2012-2013", false, Arrays.asList("1")));
        years.put("2", new Year("2", "2013-2014", true, Arrays.asList("2", "3")));
    }

    @Inject
    public YearsRest() {}

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<Year>> getYears() {
        final Map<String, Collection<Year>> yearsToReturn = new HashMap<>();

        yearsToReturn.put("years", years.values());

        return yearsToReturn;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getYear(@PathParam("id") final String id) {
        final Map<String, Object> toReturn = new HashMap<>();

        toReturn.put("year", years.get(id));

        return toReturn;
    }

    @CheckAuthorization
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Year> createYear(final Map<String, Year> payload) {
        final Year year = payload.get("year");
        final String id = IdHelper.getNextId(years.keySet());
        year.setId(id);
        years.put(id, year);

        return payload;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Year> saveYear(@PathParam("id") final String id, final Map<String, Year> payload) {
        final Year year = payload.get("year");
        final Year updated = mergeYear(years.get(id), year);

        final Map<String, Year> response = new HashMap<>();
        response.put("year", updated);

        return response;
    }

    private Year mergeYear(final Year base, final Year toMerge) {
        base.setName(toMerge.getName());
        base.setRunning(toMerge.isRunning());
        base.setClasses(toMerge.getClasses());

        return base;
    }
}
