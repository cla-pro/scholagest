package net.scholagest.managers.ontology.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.managers.ontology.OWL;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.RDFS;
import net.scholagest.namespace.CoreNamespace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class OntologyParser {
    private static Logger LOG = LogManager.getLogger(OntologyParser.class.getName());

    public Map<String, Set<OntologyElement>> parseOntology(Document document) {
        Element root = document.getDocumentElement();

        Map<String, Set<OntologyElement>> elements = new HashMap<>();
        NodeList childNodes = root.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (child instanceof Element && !(child instanceof Text)) {
                OntologyElement element = parseElement((Element) child);
                if (element != null) {
                    addElementToOntology(elements, element);
                }
            }
        }

        return elements;
    }

    private void addElementToOntology(Map<String, Set<OntologyElement>> elements, OntologyElement element) {
        Set<OntologyElement> set = elements.get(element.getType());
        if (set == null) {
            set = new HashSet<>();
            elements.put(element.getType(), set);
        }

        set.add(element);
    }

    private OntologyElement parseElement(Element element) {
        String type = resolveNamespace(element.getTagName());
        OntologyElement ontologyElement = new OntologyElementFactory().createOntologyElement(type);

        ontologyElement.setType(type);
        if (element.getAttribute("rdf:about") != null && !element.getAttribute("rdf:about").isEmpty()) {
            ontologyElement.setName(resolveNamespace(element.getAttribute("rdf:about")));
        } else if (element.getAttribute("ID") != null && !element.getAttribute("ID").isEmpty()) {
            ontologyElement.setName(resolveNamespace(element.getAttribute("ID")));
        } else if (element.getAttribute("rdf:ID") != null && !element.getAttribute("rdf:ID").isEmpty()) {
            ontologyElement.setName(resolveNamespace(element.getAttribute("rdf:ID")));
        }

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            ontologyElement.setAttribute(resolveNamespace(attribute.getNodeName()), resolveNamespace(attribute.getNodeValue()));
            LOG.debug("---- Add attribute to node (" + ontologyElement.getName() + "): " + attribute.getNodeName());
        }

        NodeList childNodes = element.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);

            if (child instanceof Element && !(child instanceof Text)) {
                OntologyElement subElement = parseElement((Element) child);
                if (subElement != null) {
                    ontologyElement.putSubElement(subElement.getType(), subElement);
                    LOG.debug("---- Add sub-element to node (" + ontologyElement.getName() + "): " + subElement.getType());
                }
            }
        }

        return ontologyElement;
    }

    /*
     * private Map<String, String> extractNamespaces(Element element) {
     * Map<String, String> namespaces = new HashMap<>();
     * 
     * NamedNodeMap attributes = element.getAttributes(); for (int i = 0; i <
     * attributes.getLength(); i++) { Node attribute = attributes.item(i);
     * String name = attribute.getNodeName();
     * 
     * if (name.startsWith("xmlns:")) {
     * namespaces.put(name.substring("xmlns:".length()),
     * attribute.getNodeValue()); } }
     * 
     * return namespaces; }
     */

    private String resolveNamespace(String text) {
        String result = text.replaceAll("rdf:", RDF.rdfNs);
        result = result.replaceAll("rdfs:", RDFS.rdfsNs);
        result = result.replaceAll("owl:", OWL.owlNs);
        result = result.replaceAll("sg:", CoreNamespace.scholagestNs + "#");

        return result;
    }
}
