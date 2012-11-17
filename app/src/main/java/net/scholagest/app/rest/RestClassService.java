package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.app.utils.JsonObject;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.services.IClassService;
import net.scholagest.services.IOntologyService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/class")
public class RestClassService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "class-";
    private final IClassService classService;
    private final IOntologyService ontologyService;

    @Inject
    public RestClassService(IClassService classService, IOntologyService ontologyService) {
        super(ontologyService);
        this.classService = classService;
        this.ontologyService = ontologyService;
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createClass(@QueryParam("token") String token, @QueryParam("yearKey") String yearKey, @QueryParam("keys") List<String> keys,
            @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to create a new
        // student.

        Map<String, Object> classInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));
        classInfo.put(CoreNamespace.pClassYear, yearKey);

        // 2. Update the database.
        String classKey = null;
        try {
            classKey = classService.createClass(requestId, classInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return new JsonObject("classKey", classKey).toString();
    }

    @GET
    @Path("/getClasses")
    @Produces("text/json")
    public String getClasses(@QueryParam("token") String token, @QueryParam("yearList") Set<String> yearKeyList,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Set<String>> classesKeySet = classService.getClasses(requestId, yearKeyList);
            Map<String, Map<String, Map<String, Object>>> classesInfo = new HashMap<>();

            for (String yearKey : classesKeySet.keySet()) {
                Map<String, Map<String, Object>> yearClassesInfo = new HashMap<>();
                for (String classKey : classesKeySet.get(yearKey)) {
                    Map<String, Object> classInfo = classService.getClassProperties(requestId, classKey, properties);
                    yearClassesInfo.put(classKey, classInfo);
                }
                classesInfo.put(yearKey, yearClassesInfo);
            }

            Gson gson = new Gson();
            String json = gson.toJson(classesInfo);
            return "{classes: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }
}
