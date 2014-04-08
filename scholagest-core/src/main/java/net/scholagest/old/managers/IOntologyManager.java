package net.scholagest.old.managers;

import java.util.Set;

import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.managers.ontology.OntologyElement;

public interface IOntologyManager {

    public OntologyElement getElementWithName(String elementName) throws DatabaseException;

    public boolean isSubtypeOf(String type, String supertype) throws DatabaseException;

    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws DatabaseException;

    public Set<String> getPropertiesForType(String type) throws DatabaseException;

}
