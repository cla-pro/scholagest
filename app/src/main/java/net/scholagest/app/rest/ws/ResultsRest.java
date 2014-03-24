package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.ResultJsonConverter;
import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.object.Result;
import net.scholagest.service.ResultServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the results (see {@link ResultJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>PUT /{id} - to update the information of a result</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/results")
public class ResultsRest {
    // public static Map<String, ResultJson> results = new HashMap<>();
    //
    // static {
    // results.put("1", new ResultJson("1", 3.5, "1", "1"));
    // results.put("2", new ResultJson("2", 5.0, "2", "1"));
    // results.put("3", new ResultJson("3", 4.25, "6", "1"));
    // results.put("4", new ResultJson("4", 3.5, "3", "2"));
    // results.put("5", new ResultJson("5", 5.0, "4", "2"));
    // results.put("6", new ResultJson("6", 5.0, "5", "2"));
    // results.put("7", new ResultJson("7", 4.25, "7", "2"));
    // }

    @Inject
    private ResultServiceLocal resultService;

    public ResultsRest() {}

    /**
     * Save the changes of the result into the system.
     * 
     * @param id Id of the updated result
     * @param payload Result's information to save
     * @return The updated result
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Map<String, Object> saveResult(@PathParam("id") final String id, final Map<String, ResultJson> payload) {
        final ResultJsonConverter converter = new ResultJsonConverter();

        final ResultJson resultJson = payload.get("result");
        final Result result = converter.convertToResult(resultJson);
        result.setId(id);

        final Result updated = resultService.saveResult(result);
        final ResultJson updatedJson = converter.convertToResultJson(updated);

        final Map<String, Object> response = new HashMap<>();
        response.put("result", updatedJson);

        return response;
    }
}
