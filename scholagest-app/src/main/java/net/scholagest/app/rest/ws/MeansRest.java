package net.scholagest.app.rest.ws;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.ws.authorization.CheckAuthorization;
import net.scholagest.app.rest.ws.converter.MeanJsonConverter;
import net.scholagest.app.rest.ws.objects.ResultJson;
import net.scholagest.object.Mean;
import net.scholagest.service.MeanServiceLocal;

import com.google.inject.Inject;

/**
 * Set methods available for rest calls (WebService) to handle the means (modeled as {@link ResultJson}). The 
 * available methods are:
 * 
 * <ul>
 *   <li>GET /{id} - to retrieve the information of a mean</li>
 *   <li>PUT /{id} - to update the information of a mean</li>
 * </ul>
 * 
 * @author CLA
 * @since 0.14.0
 */
@Path("/means")
public class MeansRest {

    @Inject
    private MeanServiceLocal meanService;

    public MeansRest() {}

    /**
     * Retrieve the information about a single mean (modeled as {@link ResultJson}) identified by its id.
     * 
     * @param id Id of the mean to get
     * @return The mean identified by id
     */
    @CheckAuthorization
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getMean(@PathParam("id") final String id) {
        final MeanJsonConverter converter = new MeanJsonConverter();

        final Mean mean = meanService.getMean(id);
        final ResultJson meanJson = converter.convertToMeanJson(mean);

        final Map<String, Object> response = new HashMap<>();
        response.put("result", meanJson);

        return response;
    }

    /**
     * Save the changes of the mean into the system.
     * 
     * @param id Id of the updated mean
     * @param payload Mean's information to save
     * @return The updated mean
     */
    @CheckAuthorization
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> saveMean(@PathParam("id") final String id, final Map<String, ResultJson> payload) {
        final MeanJsonConverter converter = new MeanJsonConverter();

        final ResultJson meanJson = payload.get("mean");
        final Mean mean = converter.convertToMean(meanJson);
        mean.setId(id);

        final Mean updated = meanService.saveMean(mean);
        final ResultJson updatedJson = converter.convertToMeanJson(updated);

        final Map<String, Object> response = new HashMap<>();
        response.put("result", updatedJson);

        return response;
    }
}
