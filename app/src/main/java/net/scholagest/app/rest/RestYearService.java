package net.scholagest.app.rest;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.utils.JsonObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IYearService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/year")
public class RestYearService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "year-";
    private final IYearService yearService;

    @Inject
    public RestYearService(IYearService yearService, IOntologyService ontologyService) {
        super(ontologyService);
        this.yearService = yearService;
    }

    @GET
    @Path("/start")
    @Produces("text/json")
    public String createStudent(@QueryParam("token") String token, @QueryParam("name") String yearName) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to start a new year.

        // 2. Update the database.
        String yearKey = null;
        try {
            yearKey = yearService.startYear(requestId, yearName);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return new JsonObject("yearKey", yearKey).toString();
    }

    @GET
    @Path("/stop")
    @Produces("text/json")
    public String createStudent(@QueryParam("token") String token) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to start a new year.

        // 2. Update the database.
        try {
            yearService.stopYear(requestId);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return new JsonObject().toString();
    }

    @GET
    @Path("/getCurrent")
    @Produces("text/json")
    public String getCurrent(@QueryParam("token") String token) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to start a new year.

        // 2. Update the database.
        String currentYearKey = null;
        try {
            currentYearKey = yearService.getCurrentYearKey(requestId);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return new JsonObject("currentYearKey", currentYearKey).toString();
    }

    @GET
    @Path("/getYears")
    @Produces("text/json")
    public String getYears(@QueryParam("token") String token, @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Map<String, Object>> yearsInfo = yearService.getYearsWithProperties(requestId, properties);
            String currentYearKey = yearService.getCurrentYearKey(requestId);

            Gson gson = new Gson();
            String years = gson.toJson(yearsInfo);

            String currentYearJson = "";
            if (currentYearKey != null) {
                currentYearJson = ", currentYear: \"" + currentYearKey + "\"";
            }
            return "{years: " + years + currentYearJson + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }
}
