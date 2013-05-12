package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.managers.impl.CoreNamespace;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.objects.ScholagestObject;
import net.scholagest.services.IOntologyService;

public class JsonConverter {
    private final IOntologyService ontologyService;

    public JsonConverter(IOntologyService ontologyService) {
        this.ontologyService = ontologyService;
    }

    public Object convertObjectToJson(ScholagestObject scholagestObject) {
        Map<String, Object> jsonObject = convertObjectToJsonInternal(scholagestObject);
        jsonObject.put("properties", convertPropertySetToJson(scholagestObject));
        return jsonObject;
    }

    public Map<String, Object> convertObjectToJson(Set<? extends ScholagestObject> scholagestObjects) {
        Map<String, Object> jsonObjects = new HashMap<>();

        for (ScholagestObject obj : scholagestObjects) {
            jsonObjects.put(obj.getKey(), convertObjectToJson(obj));
        }

        return jsonObjects;
    }

    private Map<String, Object> convertPropertySetToJson(ScholagestObject scholagestObject) {
        Map<String, Object> properties = new HashMap<>();
        for (String propertyName : scholagestObject.getProperties().keySet()) {
            properties.put(propertyName, convertPropertyToJsonWithoutOntology(scholagestObject.getProperty(propertyName)));
        }
        return properties;
    }

    public Map<String, Object> convertObjectToJson(ScholagestObject scholagestObject, Map<String, OntologyElement> ontology) throws Exception {
        Map<String, Object> jsonObject = convertObjectToJsonInternal(scholagestObject);

        jsonObject.put("properties", mergePropertiesWithOntology(scholagestObject.getProperties(), ontology));

        return jsonObject;
    }

    private Map<String, Object> convertObjectToJsonInternal(ScholagestObject scholagestObject) {
        Map<String, Object> jsonObject = new HashMap<>();

        jsonObject.put("key", scholagestObject.getKey());
        jsonObject.put("type", scholagestObject.getType());

        return jsonObject;
    }

    private Map<String, Object> convertPropertyToJsonWithoutOntology(Object value) {
        Map<String, Object> jsonProperty = new HashMap<>();

        jsonProperty.put("value", value);

        return jsonProperty;
    }

    private Map<String, Object> mergePropertiesWithOntology(Map<String, Object> properties, Map<String, OntologyElement> ontology) throws Exception {
        Map<String, Object> merged = new HashMap<>();

        for (String propertyName : properties.keySet()) {
            OntologyElement propertyDef = ontology.get(propertyName);
            Object value = properties.get(propertyName);

            if (propertyDef != null) {
                merged.put(propertyName, convertPropertyToJson(propertyDef, value));
            }
        }

        return merged;
    }

    public Map<String, Object> convertPropertyToJson(OntologyElement propertyDef, Object value) throws Exception {
        Map<String, Object> jsonProperty = new HashMap<>();

        jsonProperty.put("value", value);
        jsonProperty.put("displayText", propertyDef.getAttributes().get(CoreNamespace.scholagestNs + "#displayText"));
        if (ontologyService.isSubtypeOf(propertyDef.getAttributeWithName(RDFS.range), ScholagestNamespace.tGroup)) {
            jsonProperty.put("isHtmlGroup", true);
        }
        return jsonProperty;
    }
}
