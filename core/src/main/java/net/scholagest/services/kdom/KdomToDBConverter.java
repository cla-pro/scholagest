package net.scholagest.services.kdom;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.namespace.CoreNamespace;

public class KdomToDBConverter {
    public Map<String, Object> convertKdomToDB(IOntologyManager ontologyManager, Map<String, Object> toConvert) throws Exception {
        Map<String, Object> converted = new HashMap<>();

        for (String propertyName : toConvert.keySet()) {
            OntologyElement ontologyElement = ontologyManager.getElementWithName(propertyName);

            Object element = convertElement(ontologyElement.getAttributeWithName(RDFS.range), toConvert.get(propertyName));
            converted.put(propertyName, element);

        }

        return converted;
    }

    private Object convertElement(String attributeWithName, Object object) {
        if (attributeWithName.equals(CoreNamespace.tSet)) {

        }

        return object;
    }
}
