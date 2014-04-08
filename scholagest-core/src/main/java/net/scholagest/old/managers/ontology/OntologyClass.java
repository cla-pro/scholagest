package net.scholagest.old.managers.ontology;

import java.util.HashSet;
import java.util.Set;

public class OntologyClass extends OntologyElement {
    private Set<OntologyElement> propertiesDomain = new HashSet<>();

    private Set<OntologyElement> propertiesRange = new HashSet<>();

    public Set<OntologyElement> getPropertiesDomain() {
        return propertiesDomain;
    }

    public void setPropertiesDomain(Set<OntologyElement> propertiesDomain) {
        this.propertiesDomain = propertiesDomain;
    }

    public Set<OntologyElement> getPropertiesRange() {
        return propertiesRange;
    }

    public void setPropertiesRange(Set<OntologyElement> propertiesRange) {
        this.propertiesRange = propertiesRange;
    }
}
