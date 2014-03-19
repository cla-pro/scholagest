package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.PeriodJsonConverter;
import net.scholagest.app.rest.ws.objects.PeriodJson;
import net.scholagest.object.Period;
import net.scholagest.service.PeriodServiceLocal;

import com.google.inject.Inject;

@Path("/periods")
public class PeriodsRest {
    private final PeriodServiceLocal periodService;

    // public static Map<String, PeriodJson> periods = new HashMap<>();
    //
    // static {
    // periods.put("1", new PeriodJson("1", "Trimestre 1", "1",
    // Arrays.asList("1", "2")));
    // periods.put("2", new PeriodJson("2", "Trimestre 2", "1",
    // Arrays.asList("3", "4")));
    // periods.put("3", new PeriodJson("3", "Trimestre 3", "1",
    // Arrays.asList("5")));
    // }

    @Inject
    public PeriodsRest(final PeriodServiceLocal periodService) {
        this.periodService = periodService;
    }

    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getPeriod(@PathParam("id") final String id) {
        final PeriodJsonConverter converter = new PeriodJsonConverter();

        final Period period = periodService.getPeriod(id);
        final PeriodJson periodJson = converter.convertToPeriodJson(period);

        final Map<String, Object> response = new HashMap<>();
        response.put("period", periodJson);

        return response;
    }

    // @CheckAuthorization
    // @GET
    // @Produces(MediaType.APPLICATION_JSON)
    // public Map<String, Collection<PeriodJson>> getPeriods() {
    // final Map<String, Collection<PeriodJson>> periodsToReturn = new
    // HashMap<>();
    //
    // periodsToReturn.put("periods", periods.values());
    //
    // return periodsToReturn;
    // }
}
