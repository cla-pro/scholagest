package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/teacher")
public class RestTeacherService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "teacher-";
    private final ITeacherService teacherService;
    private final IOntologyService ontologyService;
    private JsonConverter converter;

    @Inject
    public RestTeacherService(ITeacherService teacherService, IOntologyService ontologyService) {
        super(ontologyService);
        this.teacherService = teacherService;
        this.ontologyService = ontologyService;
        this.converter = new JsonConverter(this.ontologyService);
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createTeacher(@QueryParam("token") String token, @QueryParam("teacherType") String teacherType,
            @QueryParam("keys") List<String> keys, @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        // TODO 1. Check the token and if this token allows to create a new
        // teacher.

        Map<String, Object> teacherProperties = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

        // 2. Update the database.
        BaseObject teacher = null;
        try {
            teacher = teacherService.createTeacher(requestId, teacherType, teacherProperties);

            Gson gson = new Gson();
            String json = gson.toJson(converter.convertObjectToJson(teacher, null));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getTeachers")
    @Produces("text/json")
    public String getTeachers(@QueryParam("token") String token, @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Set<BaseObject> teachers = teacherService.getTeachersWithProperties(requestId, properties);

            String json = new Gson().toJson(convertToJsonWithOntology(teachers));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getTeacherProperties(@QueryParam("token") String token, @QueryParam("teacherKey") String teacherKey,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tTeacher);
            }
            BaseObject teacherInfo = teacherService.getTeacherProperties(requestId, teacherKey, new HashSet<String>(properties));

            Map<String, OntologyElement> ontology = extractOntology(teacherInfo.getProperties().keySet());

            String json = new Gson().toJson(converter.convertObjectToJson(teacherInfo, ontology));
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/setProperties")
    @Produces("text/json")
    public String setTeacherProperties(@QueryParam("token") String token, @QueryParam("teacherKey") String teacherKey,
            @QueryParam("names") List<String> names, @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Map<String, Object> properties = JerseyHelper.listToMap(names, new ArrayList<Object>(values));

            teacherService.setTeacherProperties(requestId, teacherKey, properties);
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
