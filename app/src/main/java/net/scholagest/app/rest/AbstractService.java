package net.scholagest.app.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.IOntologyService;
import net.scholagest.utils.ConfigurationServiceImpl;
import net.scholagest.utils.ScholagestProperty;

import org.apache.shiro.ShiroException;

import com.google.inject.Inject;

public abstract class AbstractService {
    private final IOntologyService ontologyService;
    private JsonConverter converter;

    @Inject
    public AbstractService(IOntologyService ontologyService) {
        this.ontologyService = ontologyService;
        this.converter = new JsonConverter(this.ontologyService);
    }

    protected String getBaseUrl() {
        return ConfigurationServiceImpl.getInstance().getStringProperty(ScholagestProperty.BASE_URL);
    }

    protected String handleShiroException(ShiroException e) {
        Throwable cause = e.getCause();
        if (cause instanceof ScholagestException) {
            ScholagestException scholagestException = (ScholagestException) cause;
            return generateScholagestExceptionMessage(scholagestException.getErrorCode(), scholagestException.getMessage());
        } else if (cause instanceof ScholagestRuntimeException) {
            ScholagestRuntimeException scholagestException = (ScholagestRuntimeException) cause;
            return generateScholagestExceptionMessage(scholagestException.getErrorCode(), scholagestException.getMessage());
        } else {
            return generateSessionExpiredMessage(e);
        }
    }

    protected String generateSessionExpiredMessage(ShiroException e) {
        return "{errorCode:0, msg:''}";
    }

    protected String generateScholagestExceptionMessage(ScholagestExceptionErrorCode errorCode, String message) {
        return String.format("{errorCode:%d, msg:'%s'}", errorCode.getPublicCode(), message);
    }

    protected Map<String, OntologyElement> extractOntology(Set<String> properties) throws Exception {
        Map<String, OntologyElement> ontology = new HashMap<>();

        for (String propertyName : properties) {
            OntologyElement ontologyElement = extractOntology(propertyName);
            ontology.put(propertyName, ontologyElement);
        }

        return ontology;
    }

    protected OntologyElement extractOntology(String property) throws Exception {
        return ontologyService.getElementWithName(property);
    }

    protected Map<String, Object> convertToJsonWithOntology(Set<BaseObject> objects) throws Exception {
        Map<String, Object> jsonObjects = new HashMap<>();

        for (BaseObject baseObject : objects) {
            Map<String, OntologyElement> objectOntology = extractOntology(baseObject.getProperties().keySet());
            jsonObjects.put(baseObject.getKey(), converter.convertObjectToJson(baseObject, objectOntology));
        }

        return jsonObjects;
    }
}
