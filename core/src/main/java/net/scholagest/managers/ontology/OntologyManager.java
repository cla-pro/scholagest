package net.scholagest.managers.ontology;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.utils.ScholagestThreadLocal;

import com.google.inject.Inject;

public class OntologyManager implements IOntologyManager {

    @Inject
    public OntologyManager() {}

    @Override
    public OntologyElement getElementWithName(String elementName) throws DatabaseException {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String type = (String) transaction.get(elementName, RDF.type, null);

        if (type == null) {
            return null;
        }

        OntologyElement element = new OntologyElement();
        element.setType(type);
        element.setName(elementName);

        for (String col : transaction.getColumns(elementName)) {
            if (!col.equals(RDF.type)) {
                element.setAttribute(col, (String) transaction.get(elementName, col, null));
            }
        }

        return element;
    }

    @Override
    public boolean isSubtypeOf(String type, String supertype) throws DatabaseException {
        if (type.equals(supertype)) {
            return true;
        }

        OntologyElement typeElement = getElementWithName(type);
        if (typeElement == null) {
            // throw new Exception("Type \"" + type + "\" does not exists");
            // TODO
        }
        Map<String, String> subElements = typeElement.getAttributes();
        if (subElements.containsKey(RDFS.isSubclassOf)) {
            String parent = subElements.get(RDFS.isSubclassOf);
            if (parent == null) {
                return false;
            }
            if (isSubtypeOf(parent, supertype)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Set<String> filterPropertiesWithCorrectDomain(String domain, Set<String> properties) throws DatabaseException {
        Set<String> filteredProperties = new HashSet<>();

        for (String propertyName : properties) {
            OntologyElement propertyElement = getElementWithName(propertyName);
            if (propertyElement != null) {
                Map<String, String> attributes = propertyElement.getAttributes();
                String propertyDomain = attributes.get(RDFS.domain);
                if (isSubtypeOf(domain, propertyDomain)) {
                    filteredProperties.add(propertyName);
                }
            }
        }

        return filteredProperties;
    }

    @Override
    public Set<String> getPropertiesForType(String type) throws DatabaseException {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String domainPropertiesKey = (String) transaction.get(type, CoreNamespace.pOntologyClassDomain, null);
        if (domainPropertiesKey == null) {
            return new HashSet<>();
        }

        return transaction.getColumns(domainPropertiesKey);
    }
}
