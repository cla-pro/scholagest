package net.scholagest.app.rest.ws;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.objects.Result;

import com.google.inject.Inject;

@Path("/means")
public class MeansRest {

    private final ResultsRest resultsRest;

    @Inject
    public MeansRest(final ResultsRest resultsRest) {
        this.resultsRest = resultsRest;
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveMean(@PathParam("id") final String id, final Map<String, Result> result) {
        resultsRest.mergeResult(id, result.get("mean"));
    }
}
