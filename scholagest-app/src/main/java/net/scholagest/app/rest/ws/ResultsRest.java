package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    @Inject
    private ResultServiceLocal resultService;

    ResultsRest() {}

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
    public Response saveResult(@PathParam("id") final String id, final Map<String, ResultJson> payload) {
        final ResultJsonConverter converter = new ResultJsonConverter();

        final ResultJson resultJson = payload.get("result");
        final Result result = converter.convertToResult(resultJson);
        result.setId(id);

        final Result updated = resultService.saveResult(result);
        final ResultJson updatedJson = converter.convertToResultJson(updated);

        final Map<String, Object> response = new HashMap<>();
        response.put("result", updatedJson);

        return ResponseUtils.build200OkResponse(response);
    }
}
