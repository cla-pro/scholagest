package net.scholagest.app.rest.ember;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.scholagest.app.rest.AbstractService;
import net.scholagest.app.rest.ember.authorization.CheckAuthorization;
import net.scholagest.app.rest.ember.objects.Period;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/periods")
public class PeriodsRest extends AbstractService {
    public static Map<String, Period> periods = new HashMap<>();

    static {
        periods.put("1", new Period("1", "Trimestre 1", "1", Arrays.asList("1", "2")));
        periods.put("2", new Period("2", "Trimestre 2", "1", Arrays.asList("3", "4")));
        periods.put("3", new Period("3", "Trimestre 3", "1", Arrays.asList("5")));
    }

    @Inject
    public PeriodsRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @CheckAuthorization
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<Period>> getPeriods() {
        Map<String, Collection<Period>> periodsToReturn = new HashMap<>();

        periodsToReturn.put("periods", periods.values());

        return periodsToReturn;
    }
}
