package net.scholagest.old.managers.ontology;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OntologyElement {
    private String type = null;
    private String name = null;

    private Map<String, String> attributes = null;
    private Map<String, Set<OntologyElement>> subElements = null;

    public OntologyElement() {
        this.attributes = new HashMap<>();
        this.subElements = new HashMap<>();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttribute(String name, String value) {
        attributes.put(name, value);
    }

    public Map<String, Set<OntologyElement>> getSubElements() {
        return subElements;
    }

    public void putSubElement(String type, OntologyElement subElement) {
        Set<OntologyElement> set = subElements.get(type);
        if (set == null) {
            set = new HashSet<>();
            subElements.put(type, set);
        }

        set.add(subElement);
    }

    public OntologyElement getSingleSubElementWithName(String subElementName) {
        Set<OntologyElement> set = subElements.get(subElementName);
        if (set == null || set.size() == 0) {
            return null;
        } else if (set.size() >= 1) {
            return set.iterator().next();
        }

        return null;
    }

    public String getAttributeWithName(String name) {
        return attributes.get(name);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || getClass() != that.getClass()) {
            return false;
        }
        OntologyElement other = (OntologyElement) that;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (type == null) {
            if (other.type != null) {
                return false;
            }
        } else if (!type.equals(other.type)) {
            return false;
        }
        return true;
    }
}
