package net.scholagest.old.services;

import java.util.Set;

import net.scholagest.old.managers.ontology.OntologyElement;

public interface IOntologyService {
    public OntologyElement getElementWithName(String elementName) throws Exception;

    public boolean isSubtypeOf(String type, String supertype) throws Exception;

    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws Exception;

    public Set<String> getPropertiesForType(String type) throws Exception;
}
