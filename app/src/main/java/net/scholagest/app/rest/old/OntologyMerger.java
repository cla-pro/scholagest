package net.scholagest.app.rest.old;

import java.util.Map;

import net.scholagest.app.rest.ScholagestNamespace;
import net.scholagest.app.rest.old.object.RestObject;
import net.scholagest.app.rest.old.object.RestProperty;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.managers.ontology.RDFS;
import net.scholagest.old.namespace.CoreNamespace;
import net.scholagest.old.services.IOntologyService;

public class OntologyMerger {
    private final IOntologyService ontologyService;

    public OntologyMerger(IOntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    public void mergeOntologyWithRestObject(RestObject restClassInfo, Map<String, OntologyElement> ontology) throws Exception {
        Map<String, RestProperty> properties = restClassInfo.getProperties();

        for (String propertyName : properties.keySet()) {
            RestProperty property = properties.get(propertyName);

            findOntologyElementAndUpdateProperty(ontology, propertyName, property);
        }
    }

    private void findOntologyElementAndUpdateProperty(Map<String, OntologyElement> ontology, String propertyName, RestProperty property)
            throws Exception {
        OntologyElement propertyDef = ontology.get(propertyName);
        if (propertyDef != null) {
            fillPropertyWithOntology(property, propertyDef);
        }
    }

    private void fillPropertyWithOntology(RestProperty property, OntologyElement propertyDef) throws Exception {
        property.setDisplayText(propertyDef.getAttributes().get(CoreNamespace.scholagestNs + "#displayText"));

        if (ontologyService.isSubtypeOf(propertyDef.getAttributeWithName(RDFS.range), ScholagestNamespace.tSet)) {
            property.setHtmlList(true);
        }
        if (ontologyService.isSubtypeOf(propertyDef.getAttributeWithName(RDFS.range), ScholagestNamespace.tGroup)) {
            property.setHtmlGroup(true);
        }
    }
}
