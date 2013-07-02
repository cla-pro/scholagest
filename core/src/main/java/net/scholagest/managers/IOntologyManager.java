package net.scholagest.managers;

import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.managers.ontology.OntologyElement;

public interface IOntologyManager {

    public OntologyElement getElementWithName(String elementName) throws Exception;

    public boolean isSubtypeOf(String type, String supertype) throws Exception;

    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws Exception;

    public Set<String> getPropertiesForType(String type) throws DatabaseException;

}
