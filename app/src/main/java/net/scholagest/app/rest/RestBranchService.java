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
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IBranchService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IUserService;

import org.apache.shiro.subject.Subject;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/branch")
public class RestBranchService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "branch-";
    private final IBranchService branchService;
    private final IOntologyService ontologyService;
    private final IUserService userService;
    private final JsonConverter converter;

    @Inject
    public RestBranchService(IBranchService branchService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.branchService = branchService;
        this.ontologyService = ontologyService;
        this.userService = userService;
        this.converter = new JsonConverter(ontologyService);
    }

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createBranch(@QueryParam("token") String token, @QueryParam("classKey") String classKey, @QueryParam("keys") List<String> keys,
            @QueryParam("values") List<String> values) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();

        try {
            Subject subject = userService.authenticateWithToken(requestId, token);

            Map<String, Object> branchInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));

            BaseObject clazz = branchService.createBranch(requestId, classKey, branchInfo);
            RestObject restBranch = new RestToKdomConverter().restObjectFromKdom(clazz);

            String json = new Gson().toJson(restBranch);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getPropertiesForList")
    @Produces("text/json")
    public String getBranchProperties(@QueryParam("token") String token, @QueryParam("branchKeys") List<String> branchKeys,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Subject subject = userService.authenticateWithToken(requestId, token);

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tBranch);
            }

            Set<BaseObject> branchObjects = new HashSet<>();
            for (String branchKey : branchKeys) {
                BaseObject branchInfo = branchService.getBranchProperties(requestId, branchKey, new HashSet<String>(properties));
                branchObjects.add(branchInfo);
            }

            Map<String, Object> convertObjectToJson = converter.convertObjectToJson(branchObjects);

            String json = new Gson().toJson(convertObjectToJson);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getBranchProperties(@QueryParam("token") String token, @QueryParam("branchKey") String branchKey,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Subject subject = userService.authenticateWithToken(requestId, token);

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tBranch);
            }

            BaseObject branchInfo = branchService.getBranchProperties(requestId, branchKey, new HashSet<String>(properties));

            Object convertObjectToJson = converter.convertObjectToJson(branchInfo);

            String json = new Gson().toJson(convertObjectToJson);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setProperties")
    @Produces("text/json")
    public String setBranchProperties(@QueryParam("token") String token, String content) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            Subject subject = userService.authenticateWithToken(requestId, token);

            RestRequest request = new Gson().fromJson(content, RestRequest.class);
            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            branchService.setBranchProperties(requestId, requestObject.getKey(), baseObject.getProperties());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
