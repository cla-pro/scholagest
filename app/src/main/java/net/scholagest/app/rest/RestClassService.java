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

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestRequest;
import net.scholagest.app.utils.JerseyHelper;
import net.scholagest.exception.ScholagestException;
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

    @GET
    @Path("/create")
    @Produces("text/json")
    public String createClass(@QueryParam("token") String token, @QueryParam("yearKey") String yearKey, @QueryParam("keys") List<String> keys,
            @QueryParam("values") List<String> values) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Map<String, Object> classInfo = JerseyHelper.listToMap(keys, new ArrayList<Object>(values));
            classInfo.put(CoreNamespace.pClassYear, yearKey);

            BaseObject clazz = classService.createClass(classInfo, (String) classInfo.get(CoreNamespace.pClassName), yearKey);
            RestObject restClass = new RestToKdomConverter().restObjectFromKdom(clazz);

            String json = new Gson().toJson(restClass);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }
    }

    @GET
    @Path("/getClasses")
    @Produces("text/json")
    public String getClasses(@QueryParam("token") String token, @QueryParam("years") Set<String> yearKeyList,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            Map<String, Set<BaseObject>> classesKeySet = classService.getClassesForYears(yearKeyList);
            RestToKdomConverter converter = new RestToKdomConverter();

            Map<String, Set<RestObject>> classesInfo = getClassesPropertiesAndConvertToRest(properties, classesKeySet, converter);

            String json = new Gson().toJson(classesInfo);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
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

    @GET
    @Path("/getProperties")
    @Produces("text/json")
    public String getClassProperties(@QueryParam("token") String token, @QueryParam("classKey") String classKey,
            @QueryParam("properties") Set<String> properties) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());

        try {
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(token));

            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tClass);
            }

            BaseObject classInfo = classService.getClassProperties(classKey, new HashSet<String>(properties));
            Map<String, OntologyElement> ontology = extractOntology(classInfo.getProperties().keySet());

            RestObject restClassInfo = new RestToKdomConverter().restObjectFromKdom(classInfo);
            new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restClassInfo, ontology);

            String json = new Gson().toJson(restClassInfo);
            return "{info: " + json + "}";
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
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
            RestRequest request = new Gson().fromJson(content, RestRequest.class);

            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            RestObject requestObject = request.getObject();
            BaseObject baseObject = new RestToKdomConverter().baseObjectFromRest(requestObject);

            classService.setClassProperties(requestObject.getKey(), baseObject.getProperties());
        } catch (ShiroException e) {
            return generateSessionExpiredMessage(e);
        } catch (ScholagestException e) {
            return generateScholagestExceptionMessage(e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode:0, message:'" + e.getMessage() + "'}";
        }

        return "{}";
    }
}
