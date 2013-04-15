package net.scholagest.app.rest;

import java.util.ArrayList;
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

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.ITeacherService;
import net.scholagest.services.IUserService;

import org.apache.shiro.subject.Subject;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/teacher")
public class RestTeacherService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "teacher-";
    private final ITeacherService teacherService;
    private final IOntologyService ontologyService;
    private final IUserService userService;

    @Inject
    public RestTeacherService(ITeacherService teacherService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.teacherService = teacherService;
        this.ontologyService = ontologyService;
        this.userService = userService;
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createTeacher(@QueryParam("token") String token, @QueryParam("teacherType") String teacherType,
            @QueryParam("keys") List<String> keys, @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();

        try {
            Subject subject = userService.authenticateWithToken(requestId, token);

            Map<String, Object> teacherProperties = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

            BaseObject teacher = teacherService.createTeacher(requestId, teacherType, teacherProperties);
            RestObject restTeacher = new RestToKdomConverter().restObjectFromKdom(teacher);

            String json = new Gson().toJson(restTeacher);
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
            Subject subject = userService.authenticateWithToken(requestId, token);

            Set<BaseObject> teachers = teacherService.getTeachersWithProperties(requestId, properties);
            List<RestObject> restTeachers = new RestToKdomConverter().restObjectsFromKdoms(teachers);

            String json = new Gson().toJson(restTeachers);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getTeachersInfo")
    @Produces("text/json")
    public String getTeachersInfo(@QueryParam("token") String token, @QueryParam("teachers") Set<String> teacherKeyList,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();

        try {
            Subject subject = userService.authenticateWithToken(requestId, token);

            Set<BaseObject> teachers = new HashSet<>();
            for (String teacherKey : teacherKeyList) {
                BaseObject teacher = teacherService.getTeacherProperties(requestId, teacherKey, properties);

                teachers.add(teacher);
            }

            List<RestObject> restTeachers = new RestToKdomConverter().restObjectsFromKdoms(teachers);

            String json = new Gson().toJson(restTeachers);
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
            Subject subject = userService.authenticateWithToken(requestId, token);

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tTeacher);
            }
            BaseObject teacherInfo = teacherService.getTeacherProperties(requestId, teacherKey, new HashSet<String>(properties));
            Map<String, OntologyElement> ontology = extractOntology(teacherInfo.getProperties().keySet());

            RestObject restTeacherInfo = new RestToKdomConverter().restObjectFromKdom(teacherInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restTeacherInfo, ontology);

            String json = new Gson().toJson(restTeacherInfo);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setProperties")
    @Produces("text/json")
    public String setTeacherProperties(String content) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();

        try {
            RestRequest request = new Gson().fromJson(content, RestRequest.class);

            Subject subject = userService.authenticateWithToken(requestId, request.getToken());

            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            teacherService.setTeacherProperties(requestId, baseObject.getKey(), baseObject.getProperties());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
