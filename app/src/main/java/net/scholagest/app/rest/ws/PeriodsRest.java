package net.scholagest.app.rest.ws;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.Period;

import com.google.inject.Inject;

@Path("/periods")
public class PeriodsRest {
    public static Map<String, Period> periods = new HashMap<>();

    static {
        periods.put("1", new Period("1", "Trimestre 1", "1", Arrays.asList("1", "2")));
        periods.put("2", new Period("2", "Trimestre 2", "1", Arrays.asList("3", "4")));
        periods.put("3", new Period("3", "Trimestre 3", "1", Arrays.asList("5")));
    }

    @Inject
    public PeriodsRest() {}

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getPeriod(@PathParam("id") final String id) {
        final Map<String, Object> response = new HashMap<>();
        response.put("period", periods.get(id));
        return response;
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<Period>> getPeriods() {
        final Map<String, Collection<Period>> periodsToReturn = new HashMap<>();

        periodsToReturn.put("periods", periods.values());

        return periodsToReturn;
    }
}
