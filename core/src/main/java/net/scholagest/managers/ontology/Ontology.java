package net.scholagest.managers.ontology;

import java.util.HashSet;
import java.util.Set;

public class Ontology {
    private Set<OntologyElement> ontologyElementSet;

    public Ontology() {
        this.ontologyElementSet = new HashSet<>();
    }

    public void addOntologyElement(OntologyElement ontologyElement) {
        ontologyElementSet.add(ontologyElement);
    }

    public void addAllOntologyElement(Set<OntologyElement> toAdd) {
        ontologyElementSet.addAll(toAdd);
    }

    public Set<OntologyElement> getAllOntologyElement() {
        return ontologyElementSet;
    }

    public OntologyElement getElementByName(String name) {
        if (name == null) {
            return null;
        }

        for (OntologyElement ontologyElement : ontologyElementSet) {
            if (name.equals(ontologyElement.getName())) {
                return ontologyElement;
            }
        }

        return null;
    }

    public Set<OntologyElement> getElementByProperty(String propertyName, String propertyValue) {
        Set<OntologyElement> resultSet = new HashSet<>();

        for (OntologyElement ontologyElement : ontologyElementSet) {
            String value = ontologyElement.getAttributeWithName(propertyName);
            if (value == null) {
                OntologyElement subElement = ontologyElement.getSingleSubElementWithName(propertyName);
                if (subElement != null) {
                    value = subElement.getAttributeWithName(RDF.resource);
                }
            }
            if (checkValue(propertyValue, value)) {
                resultSet.add(ontologyElement);
            }
        }

        return resultSet;
    }

    private boolean checkValue(String propertyValue, String value) {
        if (value == null) {
            return propertyValue == null;
        }
        return value.equals(propertyValue);
    }

    public Set<OntologyElement> getElementByType(String type) {
        Set<OntologyElement> resultSet = new HashSet<>();

        if (type == null) {
            return resultSet;
        }

        for (OntologyElement ontologyElement : ontologyElementSet) {
            String elementType = ontologyElement.getType();
            if (type.equals(elementType)) {
                resultSet.add(ontologyElement);
            }
        }

        return resultSet;
    }
}
