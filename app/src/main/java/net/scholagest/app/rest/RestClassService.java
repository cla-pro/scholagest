package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.xml.ws.RequestWrapper;

import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IClassService;
import net.scholagest.services.IOntologyService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/class")
public class RestClassService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "class-";
    private final IClassService classService;
    private final IOntologyService ontologyService;
    private JsonConverter converter;

    @Inject
    public RestClassService(IClassService classService, IOntologyService ontologyService) {
        super(ontologyService);
        this.classService = classService;
        this.ontologyService = ontologyService;
        this.converter = new JsonConverter(this.ontologyService);
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
        BaseObject clazz = null;
        try {
            clazz = classService.createClass(requestId, classInfo);

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(clazz, null));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getClasses")
    @Produces("text/json")
    public String getClasses(@QueryParam("token") String token, @QueryParam("years") Set<String> yearKeyList,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Set<BaseObject>> classesKeySet = classService.getClassesForYears(requestId, yearKeyList);
            Map<String, Set<BaseObject>> classesInfo = new HashMap<>();

            for (String yearKey : classesKeySet.keySet()) {
                Set<BaseObject> yearClassesInfo = new HashSet<>();
                for (BaseObject clazz : classesKeySet.get(yearKey)) {
                    BaseObject classInfo = classService.getClassProperties(requestId, clazz.getKey(), properties);
                    yearClassesInfo.add(classInfo);
                }
                classesInfo.put(yearKey, yearClassesInfo);
            }

            String json = buildJsonForClasses(classesInfo);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    private String buildJsonForClasses(Map<String, Set<BaseObject>> yearClassesInfo) {
        String jsonString = "{";
        Gson gson = new Gson();

        for (String yearKey : yearClassesInfo.keySet()) {
            if (!jsonString.equals("{")) {
                jsonString += ",";
            }

            jsonString += "\"" + yearKey + "\": " + gson.toJson(converter.convertObjectToJson(yearClassesInfo.get(yearKey)));
        }

        return jsonString + "}";
    }

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getClassProperties(@QueryParam("token") String token, @QueryParam("classKey") String classKey,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            if (properties == null || properties.isEmpty()) {
                properties = this.ontologyService.getPropertiesForType(CoreNamespace.tClass);
            }
            BaseObject classInfo = classService.getClassProperties(requestId, classKey, new HashSet<String>(properties));

            Map<String, OntologyElement> ontology = extractOntology(classInfo.getProperties().keySet());

            String json = new Gson().toJson(converter.convertObjectToJson(classInfo, ontology));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @SuppressWarnings("unchecked")
    @RequestWrapper(className = "net.scholagest.app.rest.jaxws.SetClassProperties", localName = "setClassProperties",
            targetNamespace = "http://rest.app.scholagest.net/")
    @POST
    @Path("/setProperties")
    @Produces("text/json")
    public String setClassProperties(@QueryParam("token") String token, String receivedContent) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Object> fromJson = new Gson().fromJson(receivedContent, Map.class);
            System.out.println(fromJson);
            // Map<String, Object> properties = JerseyHelper.listToMap(names,
            // new ArrayList<Object>(values));

            // classService.setClassProperties(requestId, classKey, properties);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
