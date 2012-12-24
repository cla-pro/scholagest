package net.scholagest.app.rest;

import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IYearService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/year")
public class RestYearService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "year-";
    private final IYearService yearService;
    private JsonConverter converter;

    @Inject
    public RestYearService(IYearService yearService, IOntologyService ontologyService) {
        super(ontologyService);
        this.yearService = yearService;
        this.converter = new JsonConverter(ontologyService);
    }

    @GET
    @Path("/start")
    @Produces("text/json")
    public String startYear(@QueryParam("token") String token, @QueryParam("name") String yearName) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to start a new year.

        // 2. Update the database.
        BaseObject year = null;
        try {
            year = yearService.startYear(requestId, yearName);

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(year, null));
            return "{info: " + json + "}";

        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/stop")
    @Produces("text/json")
    public String stopYear(@QueryParam("token") String token) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to start a new year.

        // 2. Update the database.
        try {
            yearService.stopYear(requestId);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @GET
    @Path("/getCurrent")
    @Produces("text/json")
    public String getCurrent(@QueryParam("token") String token) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to start a new year.

        // 2. Update the database.
        BaseObject currentYear = null;
        try {
            currentYear = yearService.getCurrentYearKey(requestId);

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(currentYear, null));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getYears")
    @Produces("text/json")
    public String getYears(@QueryParam("token") String token, @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Set<BaseObject> yearsInfo = yearService.getYearsWithProperties(requestId, properties);
            BaseObject currentYearKey = yearService.getCurrentYearKey(requestId);

            Gson gson = new Gson();
            String years = gson.toJson(converter.convertObjectToJson(yearsInfo));

            String currentYearJson = "";
            if (currentYearKey != null) {
                currentYearJson = ", currentYear: " + gson.toJson(converter.convertObjectToJson(currentYearKey)) + "";
            }
            return "{info: {years: " + years + currentYearJson + "}}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }
}
