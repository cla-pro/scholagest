package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.PeriodJsonConverter;
import net.scholagest.app.rest.ws.objects.PeriodJson;
import net.scholagest.object.Period;
import net.scholagest.service.PeriodServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the periods (see {@link PeriodJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>GET ids[] - to retrieve a list of periods filtered by the ids</li>
 *   <li>GET /{id} - to retrieve the information of a period</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/periods")
public class PeriodsRest {

    @Inject
    private PeriodServiceLocal periodService;

    PeriodsRest() {}

    /**
     * Retrieve the information about a single period identified by its id.
     * 
     * @param id Id of the period to get
     * @return The period identified by id
     */
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

    /**
     * <p>
     * Retrieve a list of {@link Period}s filtered by ids. The ids are specified as query parameter with the name "ids[]".
     * </p>
     * 
     * <p>
     * Examples:
     * <ul>
     *   <li>Filter the periods by id: GET base_url/periods?ids[]=1&ids[]=2</li>
     * </ul>
     * </p>
     * 
     * @param ids Parameter used to filter the list of periods
     * @return The list of periods filtered by ids
     */
    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getPeriods(@QueryParam("ids[]") final List<String> ids) {
        final PeriodJsonConverter converter = new PeriodJsonConverter();

        final List<Period> periodList = periodService.getPeriods(ids);
        final List<PeriodJson> periodJsonList = converter.convertToPeriodJsonList(periodList);

        final Map<String, Object> response = new HashMap<>();
        response.put("periods", periodJsonList);

        return response;
    }
}
