package net.scholagest.old.business.impl;

import java.util.Set;

import net.scholagest.old.business.IOntologyBusinessComponent;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.ontology.OntologyElement;

import com.google.inject.Inject;

public class OntologyBusinessComponent implements IOntologyBusinessComponent {
    private IOntologyManager ontologyManager;

    @Inject
    public OntologyBusinessComponent(IOntologyManager ontologyManager) {
        this.ontologyManager = ontologyManager;
    }

    @Override
    public OntologyElement getElementWithName(String elementName) throws Exception {
        return ontologyManager.getElementWithName(elementName);
    }

    @Override
    public boolean isSubtypeOf(String type, String supertype) throws Exception {
        return ontologyManager.isSubtypeOf(type, supertype);
    }

    @Override
    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws Exception {
        return ontologyManager.filterPropertiesWithCorrectDomain(domain, properties);
    }

    @Override
    public Set<String> getPropertiesForType(String type) throws DatabaseException {
        return ontologyManager.getPropertiesForType(type);
    }
}
