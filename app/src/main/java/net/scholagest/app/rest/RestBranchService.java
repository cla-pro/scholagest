package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.scholagest.app.rest.object.RestGetMeansBranchRequest;
import net.scholagest.app.rest.object.RestGetObjectRequest;
import net.scholagest.app.rest.object.RestGetObjectSubListRequest;
import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestSetObjectRequest;
import net.scholagest.app.rest.object.create.RestCreateBranchRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IBranchService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;

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

    @POST
    @Path("/create")
    @Produces("text/json")
    public String createBranch(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestCreateBranchRequest request = new Gson().fromJson(content, RestCreateBranchRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Object> branchInfo = JerseyHelper.listToMap(request.getKeys(), new ArrayList<Object>(request.getValues()));

            BaseObject clazz = branchService.createBranch(request.getClassKey(), branchInfo);
            RestObject restBranch = new RestToKdomConverter().restObjectFromKdom(clazz);

            String json = new Gson().toJson(restBranch);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/getBranchesInfo")
    @Produces("text/json")
    public String getBranchListProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestGetObjectSubListRequest request = new Gson().fromJson(content, RestGetObjectSubListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tBranch);
            }

            Set<BaseObject> branchObjects = new HashSet<>();
            for (String branchKey : request.getKeys()) {
                BaseObject branchInfo = branchService.getBranchProperties(branchKey, new HashSet<String>(properties));
                branchObjects.add(branchInfo);
            }

            Map<String, Object> convertObjectToJson = converter.convertObjectToJson(branchObjects);

            String json = new Gson().toJson(convertObjectToJson);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/getProperties")
    @Produces("text/json")
    public String getBranchProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestGetObjectRequest request = new Gson().fromJson(content, RestGetObjectRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (request.arePropertiesEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tBranch);
            }

            BaseObject branchInfo = branchService.getBranchProperties(request.getKey(), properties);

            Object convertObjectToJson = converter.convertObjectToJson(branchInfo);

            String json = new Gson().toJson(convertObjectToJson);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @POST
    @Path("/setProperties")
    @Produces("text/json")
    public String setBranchProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestSetObjectRequest request = new Gson().fromJson(content, RestSetObjectRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            branchService.setBranchProperties(request.getKey(), request.getProperties());
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }

    @POST
    @Path("/getMeans")
    @Produces("text/json")
    public String getMeans(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestGetMeansBranchRequest request = new Gson().fromJson(content, RestGetMeansBranchRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Map<String, BaseObject>> means = branchService.getBranchMeans(request.getBranchKey(), request.getStudentKeys());

            Map<String, Object> jsonMeans = new HashMap<>();
            String meanKey = means.keySet().iterator().next();
            jsonMeans.put("key", meanKey);
            jsonMeans.put("grades", new RestToKdomConverter().mapRestObjectFromMapKdom(means.get(meanKey)));

            String json = new Gson().toJson(jsonMeans);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return handleShiroException(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (ScholagestRuntimeException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }
}
