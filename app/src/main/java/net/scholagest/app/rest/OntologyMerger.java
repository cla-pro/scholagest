package net.scholagest.app.rest;

import java.util.Map;

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestProperty;
import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.services.IOntologyService;

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
