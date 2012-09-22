package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.managers.CoreNamespace;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.managers.ontology.parser.OntologyElement;
import net.scholagest.services.IOntologyService;

import com.google.inject.Inject;

public abstract class AbstractService {
    private final IOntologyService ontologyService;

    @Inject
    public AbstractService(IOntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Map<String, Object>> extractOntology(Map<String, Object> info) throws Exception {
        Map<String, Map<String, Object>> result = new HashMap<>();
        for (Map.Entry<String, Object> entry : info.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Map) {
                value = extractOntology((Map<String, Object>) value);
            }
            // Get ontology for the field.
            OntologyElement element = ontologyService.getElementWithName(entry.getKey());
            Map<String, Object> fieldInfo = new HashMap<>();
            fieldInfo.put("value", value);
            String displayText = element.getAttributes().get(CoreNamespace.scholagestNs + "#displayText");
            fieldInfo.put("displayText", displayText);
            boolean isRangeGroup = ontologyService.isSubtypeOf(element.getAttributeWithName(RDFS.range), ScholagestNamespace.tGroup);
            if (isRangeGroup) {
                fieldInfo.put("isHtmlGroup", true);
            }

            result.put(entry.getKey(), fieldInfo);
        }

        return result;
    }
}
