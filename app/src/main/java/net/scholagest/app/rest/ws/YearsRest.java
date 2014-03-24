package net.scholagest.app.rest.ws;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.YearJsonConverter;
import net.scholagest.app.rest.ws.objects.YearJson;
import net.scholagest.object.Year;
import net.scholagest.service.YearServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the years. The available methods are:
 * 
 * <ul>
 *   <li>GET - to retrieve the list of all the years</li>
 *   <li>GET /{id} - to retrieve the information of a single year</li>
 *   <li>PUT /{id} - to update the information of a year</li>
 *   <li>POST - to create a new year</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/years")
public class YearsRest {
    // public static Map<String, YearJson> years = new HashMap<>();
    //
    // static {
    // years.put("1", new YearJson("1", "2012-2013", false,
    // Arrays.asList("1")));
    // years.put("2", new YearJson("2", "2013-2014", true, Arrays.asList("2",
    // "3")));
    // }
    private final YearServiceLocal yearService;

    @Inject
    public YearsRest(final YearServiceLocal yearService) {
        this.yearService = yearService;
    }

    /**
     * Retrieve a list of all the years.
     * 
     * @return The list of all the years
     */
    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<YearJson>> getYears() {
        final YearJsonConverter converter = new YearJsonConverter();

        final List<Year> years = yearService.getYears();
        final List<YearJson> yearsJson = converter.convertToYearJsonList(years);

        final Map<String, Collection<YearJson>> response = new HashMap<>();
        response.put("years", yearsJson);

        return response;
    }

    /**
     * Retrieve the information about a single year identified by its id.
     * 
     * @param id Id of the year to get
     * @return The year identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getYear(@PathParam("id") final String id) {
        final YearJsonConverter converter = new YearJsonConverter();
        final Map<String, Object> response = new HashMap<>();

        final Year year = yearService.getYear(id);
        if (year != null) {
            final YearJson yearJson = converter.convertToYearJson(year);
            response.put("year", yearJson);
        }

        return response;
    }

    /**
     * Create a new year.
     * 
     * @param payload The year's information to save on creation
     * @return The newly created year
     */
    @CheckAuthorization
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, YearJson> createYear(final Map<String, YearJson> payload) {
        final YearJsonConverter converter = new YearJsonConverter();

        final YearJson yearJson = payload.get("year");
        final Year year = converter.convertToYear(yearJson);

        final Year createdYear = yearService.createYear(year);
        final YearJson createdYearJson = converter.convertToYearJson(createdYear);

        final Map<String, YearJson> response = new HashMap<>();
        response.put("year", createdYearJson);

        return response;
    }

    /**
     * Save the changes of the year into the system.
     * 
     * @param id Id of the updated year
     * @param payload Year's information to save
     * @return The updated year
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, YearJson> saveYear(@PathParam("id") final String id, final Map<String, YearJson> payload) {
        final YearJsonConverter converter = new YearJsonConverter();

        final YearJson yearJson = payload.get("year");
        final Year year = converter.convertToYear(yearJson);
        year.setId(id);

        final Year updatedYear = yearService.saveYear(year);
        final YearJson updatedYearJson = converter.convertToYearJson(updatedYear);

        final Map<String, YearJson> response = new HashMap<>();
        response.put("year", updatedYearJson);

        return response;
    }
}
