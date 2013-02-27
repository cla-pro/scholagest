package net.scholagest.app.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.scholagest.app.rest.object.RestObject;
import net.scholagest.app.rest.object.RestProperty;
import net.scholagest.objects.BaseObject;
import net.scholagest.services.kdom.KSet;

public class RestToKdomConverter {
    public BaseObject baseObjectFromRest(RestObject restObject) {
        BaseObject baseObject = new BaseObject(restObject.getKey(), restObject.getType());
        baseObject.setProperties(extractProperties(restObject.getProperties()));

        return baseObject;
    }

    private Map<String, Object> extractProperties(Map<String, RestProperty> restProperties) {
        Map<String, Object> properties = new HashMap<>();

        for (String propertyName : restProperties.keySet()) {
            properties.put(propertyName, restProperties.get(propertyName).getValue());
        }

        return properties;
    }

    public List<RestObject> restObjectsFromKdoms(Collection<BaseObject> baseObjects) {
        List<RestObject> restObjects = new ArrayList<>();

        for (BaseObject baseObject : baseObjects) {
            if (baseObject == null) {
                restObjects.add(null);
            } else {
                restObjects.add(restObjectFromKdom(baseObject));
            }
        }

        return restObjects;
    }

    public RestObject restObjectFromKdom(BaseObject baseObject) {
        RestObject restObject = new RestObject();

        restObject.setKey(baseObject.getKey());
        restObject.setType(baseObject.getType());
        restObject.setProperties(convertProperties(baseObject.getProperties()));

        return restObject;
    }

    private Map<String, RestProperty> convertProperties(Map<String, Object> properties) {
        Map<String, RestProperty> restProperties = new HashMap<>();

        for (String propertyName : properties.keySet()) {
            RestProperty restProperty = new RestProperty();
            restProperty.setValue(convertPropertyValue(properties.get(propertyName)));
            restProperties.put(propertyName, restProperty);
        }

        return restProperties;
    }

    private Object convertPropertyValue(Object object) {
        Object result = object;
        if (object instanceof KSet) {
            result = ((KSet) object).getValues();
        }

        return result;
    }
}
