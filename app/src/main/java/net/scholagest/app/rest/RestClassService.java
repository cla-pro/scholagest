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

import net.scholagest.app.rest.object.RestGetObjectListClassRequest;
import net.scholagest.app.rest.object.RestGetObjectRequest;
import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestSetObjectRequest;
import net.scholagest.app.rest.object.create.RestCreateClassRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IClassService;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/class")
public class RestClassService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "class-";
    private final IClassService classService;
    private final IOntologyService ontologyService;
    private final IUserService userService;

    @Inject
    public RestClassService(IClassService classService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.classService = classService;
        this.ontologyService = ontologyService;
        this.userService = userService;
    }

    @POST
    @Path("/create")
    @Produces("text/json")
    public String createClass(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestCreateClassRequest request = new Gson().fromJson(content, RestCreateClassRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Object> classInfo = JerseyHelper.listToMap(request.getKeys(), new ArrayList<Object>(request.getValues()));
            classInfo.put(CoreNamespace.pClassYear, request.getYearKey());

            BaseObject clazz = classService.createClass(classInfo, (String) classInfo.get(CoreNamespace.pClassName), request.getYearKey());
            RestObject restClass = new RestToKdomConverter().restObjectFromKdom(clazz);

            String json = new Gson().toJson(restClass);
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
    @Path("/getClasses")
    @Produces("text/json")
    public String getClasses(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectListClassRequest request = new Gson().fromJson(content, RestGetObjectListClassRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Set<BaseObject>> classesKeySet = classService.getClassesForYears(request.getYearKeys());
            RestToKdomConverter converter = new RestToKdomConverter();

            Map<String, Set<RestObject>> classesInfo = getClassesPropertiesAndConvertToRest(request.getProperties(), classesKeySet, converter);

            String json = new Gson().toJson(classesInfo);
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

    private Map<String, Set<RestObject>> getClassesPropertiesAndConvertToRest(Set<String> properties, Map<String, Set<BaseObject>> classesKeySet,
            RestToKdomConverter converter) throws Exception {
        Map<String, Set<RestObject>> classesInfo = new HashMap<>();

        for (String yearKey : classesKeySet.keySet()) {
            Set<RestObject> yearClassesInfo = new HashSet<>();
            for (BaseObject clazz : classesKeySet.get(yearKey)) {
                BaseObject classInfo = classService.getClassProperties(clazz.getKey(), properties);
                yearClassesInfo.add(converter.restObjectFromKdom(classInfo));
            }
            classesInfo.put(yearKey, yearClassesInfo);
        }

        return classesInfo;
    }

    @POST
    @Path("/getProperties")
    @Produces("text/json")
    public String getClassProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestGetObjectRequest request = new Gson().fromJson(content, RestGetObjectRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (request.arePropertiesEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tClass);
            }

            BaseObject classInfo = classService.getClassProperties(request.getKey(), new HashSet<String>(properties));
            Map<String, OntologyElement> ontology = extractOntology(classInfo.getProperties().keySet());

            RestObject restClassInfo = new RestToKdomConverter().restObjectFromKdom(classInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restClassInfo, ontology);

            String json = new Gson().toJson(restClassInfo);
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
    public String setClassProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            RestSetObjectRequest request = new Gson().fromJson(content, RestSetObjectRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            classService.setClassProperties(request.getKey(), request.getProperties());
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
}
