package net.scholagest.services.kdom;

import java.util.HashMap;
import java.util.Map;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.RDFS;

public class KdomToDBConverter {
    public Map<String, Object> convertKdomToDB(String requestId, ITransaction transaction, OntologyManager ontologyManager,
            Map<String, Object> toConvert) throws Exception {
        Map<String, Object> converted = new HashMap<>();

        for (String propertyName : toConvert.keySet()) {
            OntologyElement ontologyElement = ontologyManager.getElementWithName(requestId, transaction, propertyName);

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
