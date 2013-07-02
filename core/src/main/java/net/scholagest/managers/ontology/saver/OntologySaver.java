package net.scholagest.managers.ontology.saver;

import java.util.Map;
import java.util.Set;

import net.scholagest.database.DatabaseException;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.Ontology;
import net.scholagest.managers.ontology.OntologyClass;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.namespace.CoreNamespace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OntologySaver {
    private static Logger LOG = LogManager.getLogger(OntologySaver.class.getName());

    public void saveOntology(String requestId, ITransaction transaction, Ontology ontology) throws Exception {
        for (OntologyElement element : ontology.getAllOntologyElement()) {
            this.saveElement(requestId, transaction, element);

            transaction.insert(CoreNamespace.ontologyBase, element.getName(), element.getName(), null);
        }
    }

    private void saveElement(String requestId, ITransaction transaction, OntologyElement element) throws Exception {
        transaction.insert(element.getName(), RDF.type, element.getType(), null);

        if (element instanceof OntologyClass) {
            saveClassElement(requestId, transaction, (OntologyClass) element);
        }

        for (Map.Entry<String, Set<OntologyElement>> subElement : element.getSubElements().entrySet()) {
            for (OntologyElement sub : subElement.getValue()) {
                if (sub.getAttributes().containsKey(RDF.resource)) {
                    LOG.debug("Insert sub-element " + sub.getType() + " on node " + element.getName());
                    transaction.insert(element.getName(), sub.getType(), sub.getAttributes().get(RDF.resource), null);
                }
            }
        }
    }

    private void saveClassElement(String requestId, ITransaction transaction, OntologyClass element) throws Exception {
        savePropertiesLinkedToClass(requestId, transaction, element.getName(), CoreNamespace.pOntologyClassDomain, element.getName()
                + "PropetiesWithDomain", element.getPropertiesDomain());
        savePropertiesLinkedToClass(requestId, transaction, element.getName(), CoreNamespace.pOntologyClassRange, element.getName()
                + "PropetiesWithRange", element.getPropertiesRange());
    }

    private void savePropertiesLinkedToClass(String requestId, ITransaction transaction, String nodeName, String propertyName, String targetNodeName,
            Set<OntologyElement> properties) throws DatabaseException {
        transaction.insert(nodeName, propertyName, targetNodeName, null);

        for (OntologyElement ontologyElement : properties) {
            transaction.insert(targetNodeName, ontologyElement.getName(), ontologyElement.getName(), null);
        }
    }
}
