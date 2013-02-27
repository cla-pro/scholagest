package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.services.IPeriodService;

import com.google.gson.Gson;
import com.google.inject.Inject;

@Path("/period")
public class RestPeriodService extends AbstractService {
    private final static String REQUEST_ID_PREFIX = "period-";
    private final IPeriodService periodService;
    private final IOntologyService ontologyService;
    private final JsonConverter converter;

    @Inject
    public RestPeriodService(IPeriodService periodService, IOntologyService ontologyService) {
        super(ontologyService);
        this.periodService = periodService;
        this.ontologyService = ontologyService;
        this.converter = new JsonConverter(ontologyService);
    }

    @GET
    @Path("/getPropertiesForList")
    @Produces("text/json")
    public String getPeriodProperties(@QueryParam("token") String token, @QueryParam("periodKeys") List<String> periodKeys,
            @QueryParam("properties") Set<String> properties) {
        String requestId = REQUEST_ID_PREFIX + UUID.randomUUID();
        try {
            if (properties == null || properties.isEmpty()) {
                properties = ontologyService.getPropertiesForType(CoreNamespace.tPeriod);
            }

            Set<BaseObject> periodObjects = new HashSet<>();
            Map<String, Object> jsonObjects = new HashMap<>();
            for (String periodKey : periodKeys) {
                BaseObject periodInfo = periodService.getPeriodProperties(requestId, periodKey, new HashSet<String>(properties));

                Map<String, OntologyElement> ontology = extractOntology(periodInfo.getProperties().keySet());
                RestObject restPeriodInfo = new RestToKdomConverter().restObjectFromKdom(periodInfo);
                new OntologyMerger(ontologyService).mergeOntologyWithRestObject(restPeriodInfo, ontology);

                jsonObjects.put(periodKey, restPeriodInfo);
            }

            String json = new Gson().toJson(jsonObjects);
            return "{info: " + json + "}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{errorCode=0, message='" + e.getMessage() + "'}";
        }
    }
}
