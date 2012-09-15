package net.scholagest.services;

import java.util.Set;

import net.scholagest.managers.ontology.parser.OntologyElement;

public interface IOntologyService {
    public OntologyElement getElementWithName(String elementName) throws Exception;

    public boolean isSubtypeOf(String type, String supertype) throws Exception;

    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws Exception;
}
