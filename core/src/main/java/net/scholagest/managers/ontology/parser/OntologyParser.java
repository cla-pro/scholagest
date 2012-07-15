package net.scholagest.managers.ontology.parser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.scholagest.managers.ontology.OWL;
import net.scholagest.managers.ontology.RDF;
import net.scholagest.managers.ontology.RDFS;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class OntologyParser {
	public Map<String, Set<OntologyElement>> parseOntology(Document document) {
		Element root = document.getDocumentElement();
		
		Map<String, Set<OntologyElement>> elements = new HashMap<>();
		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			
			if (child instanceof Element &&
					!(child instanceof Text)) {
				OntologyElement element = this.parseElement((Element) child);
				if (element != null) {
					Set<OntologyElement> set = elements.get(element.getType());
					if (set == null) {
						set = new HashSet<>();
						elements.put(element.getType(), set);
					}
					
					set.add(element);
				}
			}
		}
		
		return elements;
	}
	
	private OntologyElement parseElement(Element element) {
		OntologyElement ontologyElement = new OntologyElement();
		
		ontologyElement.setType(this.resolveNamespace(element.getTagName()));
		if (element.getAttribute("rdf:about") != null
				&& !element.getAttribute("rdf:about").isEmpty())
			ontologyElement.setName(element.getAttribute("rdf:about"));
		else if (element.getAttribute("ID") != null
				&& !element.getAttribute("ID").isEmpty())
			ontologyElement.setName(element.getAttribute("ID"));
		else if (element.getAttribute("rdf:ID") != null
				&& !element.getAttribute("rdf:ID").isEmpty())
			ontologyElement.setName(element.getAttribute("rdf:ID"));
		
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			ontologyElement.setAttribute(
					this.resolveNamespace(attribute.getNodeName()),
					attribute.getNodeValue());
		}
		
		NodeList childNodes = element.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node child = childNodes.item(i);
			
			if (child instanceof Element &&
					!(child instanceof Text)) {
				OntologyElement subElement = this.parseElement((Element) child);
				if (subElement != null)
					ontologyElement.putSubElement(subElement.getType(),
							subElement);
			}
		}
		
		return ontologyElement;
	}
	
	/*private Map<String, String> extractNamespaces(Element element) {
		Map<String, String> namespaces = new HashMap<>();
		
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Node attribute = attributes.item(i);
			String name = attribute.getNodeName();
			
			if (name.startsWith("xmlns:")) {
				namespaces.put(name.substring("xmlns:".length()),
						attribute.getNodeValue());
			}
		}
		
		return namespaces;
	}*/
	
	private String resolveNamespace(String text) {
		String result = text.replaceAll("rdf:", RDF.rdfNs);
		result = result.replaceAll("rdfs:", RDFS.rdfsNs);
		result = result.replaceAll("owl:", OWL.owlNs);
		
		return result;
	}
}
