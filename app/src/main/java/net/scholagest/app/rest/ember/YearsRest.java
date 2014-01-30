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
import net.scholagest.app.rest.ember.objects.Year;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

@Path("/years")
public class YearsRest extends AbstractService {
    public static Map<String, Year> years = new HashMap<>();

    static {
        years.put("1", new Year("1", "2012-2013", Arrays.asList("1")));
        years.put("2", new Year("2", "2013-2014", Arrays.asList("2", "3")));
    }

    @Inject
    public YearsRest(IOntologyService ontologyService) {
        super(ontologyService);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Collection<Year>> getYears() {
        Map<String, Collection<Year>> yearsToReturn = new HashMap<>();

        yearsToReturn.put("years", years.values());

        return yearsToReturn;
    }
}
