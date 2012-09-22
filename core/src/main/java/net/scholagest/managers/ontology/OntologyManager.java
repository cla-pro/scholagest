package net.scholagest.managers.ontology;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.parser.OntologyElement;

import com.google.inject.Inject;

public class OntologyManager {

    @Inject
    public OntologyManager() {}

    public OntologyElement getElementWithName(String requestId, ITransaction transaction, String elementName) throws Exception {
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

    public boolean isSubtypeOf(String requestId, ITransaction transaction, String type, String supertype) throws Exception {
        if (type.equals(supertype)) {
            return true;
        }

        OntologyElement typeElement = getElementWithName(requestId, transaction, type);
        if (typeElement == null) {
            throw new Exception("Type \"" + type + "\" does not exists");
        }
        Map<String, String> subElements = typeElement.getAttributes();
        if (subElements.containsKey(RDFS.isSubclassOf)) {
            String parent = subElements.get(RDFS.isSubclassOf);
            if (parent == null) {
                return false;
            }
            if (isSubtypeOf(requestId, transaction, parent, supertype)) {
                return true;
            }
        }

        return false;
    }

    public Set<String> filterPropertiesWithCorrectDomain(String requestId, ITransaction transaction, String domain, Set<String> properties)
            throws Exception {
        Set<String> filteredProperties = new HashSet<>();

        for (String propertyName : properties) {
            OntologyElement propertyElement = getElementWithName(requestId, transaction, propertyName);
            if (propertyElement != null) {
                Map<String, String> attributes = propertyElement.getAttributes();
                String propertyDomain = attributes.get(RDFS.domain);
                if (isSubtypeOf(requestId, transaction, domain, propertyDomain)) {
                    filteredProperties.add(propertyName);
                }
            }
        }

        return filteredProperties;
    }
}
