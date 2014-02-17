package net.scholagest.app.rest.ember;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Result;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/means")
public class MeansRest extends AbstractService {

    private ResultsRest resultsRest;

    @Inject
    public MeansRest(IOntologyService ontologyService, ResultsRest resultsRest) {
        super(ontologyService);
        this.resultsRest = resultsRest;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveMean(@PathParam("id") String id, Map<String, Result> result) {
        resultsRest.mergeResult(id, result.get("mean"));
    }
}
