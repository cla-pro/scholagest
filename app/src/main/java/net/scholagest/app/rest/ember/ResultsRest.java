package net.scholagest.app.rest.ember;

import java.util.HashMap;
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

@Path("/results")
public class ResultsRest extends AbstractService {
    public static Map<String, Result> results = new HashMap<>();

    static {
        results.put("1", new Result("1", 3.5, "1", "1"));
        results.put("2", new Result("2", 5, "2", "1"));
        results.put("3", new Result("3", 4.25, "6", "1"));
        results.put("4", new Result("4", 3.5, "3", "2"));
        results.put("5", new Result("5", 5, "4", "2"));
        results.put("6", new Result("6", 5, "5", "2"));
        results.put("7", new Result("7", 4.25, "7", "2"));
    }

    @Inject
    public ResultsRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveResult(@PathParam("id") String id, Map<String, Result> result) {
        mergeResult(id, result.get("result"));
    }

    void mergeResult(String id, Result result) {
        final Result toBeMerged = results.get(id);
        toBeMerged.setGrade(result.getGrade());
    }
}
