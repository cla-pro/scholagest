package net.scholagest.app.rest.old;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import net.scholagest.app.rest.old.object.RestGetMeansPeriodRequest;
import net.scholagest.app.rest.old.object.RestGetObjectSubListRequest;
import net.scholagest.app.rest.old.object.RestObject;
import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.objects.BaseObject;
import net.scholagest.old.services.IOntologyService;
import net.scholagest.old.services.IPeriodService;
import net.scholagest.old.services.IUserService;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.shiro.ShiroException;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/period")
public class RestPeriodService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "period-";
    private final IPeriodService periodService;
    private final IOntologyService ontologyService;
    private final IUserService userService;

    @Inject
    public RestPeriodService(IPeriodService periodService, IOntologyService ontologyService, IUserService userService) {
        super(ontologyService);
        this.periodService = periodService;
        this.ontologyService = ontologyService;
        this.userService = userService;
    }

    @POST
    @Path("/getPeriodsInfo")
    @Produces("text/json")
    public String getPeriodProperties(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestGetObjectSubListRequest request = new Gson().fromJson(content, RestGetObjectSubListRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Set<String> properties = request.getProperties();
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tPeriod);
            }

            Map<String, Object> jsonObjects = new HashMap<>();
            for (String periodKey : request.getKeys()) {
                BaseObject periodInfo = periodService.getPeriodProperties(periodKey, new HashSet<String>(properties));

                Map<String, OntologyElement> ontology = extractOntology(periodInfo.getProperties().keySet());
                RestObject restPeriodInfo = new RestToKdomConverter().restObjectFromKdom(periodInfo);
                new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restPeriodInfo, ontology);

                jsonObjects.put(periodKey, restPeriodInfo);
            }

            String json = new Gson().toJson(jsonObjects);
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
    @Path("/getMeans")
    @Produces("text/json")
    public String getMeans(String content) {
        ScholagestThreadLocal.setRequestId(REQUEST_ID_PREFIX + UUID.randomUUID());
        try {
            RestGetMeansPeriodRequest request = new Gson().fromJson(content, RestGetMeansPeriodRequest.class);
            ScholagestThreadLocal.setSubject(userService.authenticateWithToken(request.getToken()));

            Map<String, Map<String, BaseObject>> means = periodService.getPeriodMeans(request.getPeriodKey(), request.getStudentKeys());

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
