package net.scholagest.managers.ontology;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.parser.OntologyElement;
import net.scholagest.managers.ontology.parser.OntologyParser;
import net.scholagest.managers.ontology.saver.OntologySaver;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.google.inject.Inject;

public class OntologyManager {
	private DocumentBuilderFactory docBuilderFactory = null;
	
	@Inject
	public OntologyManager() {
		this.docBuilderFactory = DocumentBuilderFactory.newInstance();
		this.docBuilderFactory.setIgnoringComments(true);
		this.docBuilderFactory.setNamespaceAware(true);
	}
	
	public Map<String, Set<OntologyElement>> compileOntology(String requestId,
			ITransaction transaction, Document xmlOntology) throws Exception {
		//Parse the ontology.
		OntologyParser parser = new OntologyParser();
		Map<String, Set<OntologyElement>> elements
				= parseAndLoadOntology(xmlOntology, parser);
		
		for (Set<OntologyElement> elementSet : elements.values()) {
			for (OntologyElement ontologyElement : elementSet) {
				System.out.println("---- " + ontologyElement.getName());
			}
		}
		
		//TODO Solve the names.
		
		
		// Store into the database.
		//new OntologySaver().saveOntology(requestId, transaction, elements);
		
		return elements;
	}
	
	private Map<String, Set<OntologyElement>> parseAndLoadOntology(
			Document xmlOntology, OntologyParser parser) {
		Map<String, Set<OntologyElement>> elements = new HashMap<>();
		elements.putAll(parser.parseOntology(xmlOntology));
		
		//Search for imports.
		Set<OntologyElement> owlOntologies = elements.get(OWL.ontology);
		if (owlOntologies != null && owlOntologies.size() > 0) {
			for (OntologyElement owlOnto : owlOntologies) {
				Set<OntologyElement> imports =
						owlOnto.getSubElements().get(OWL.imports);
				
				if (imports == null)
					continue;
				
				for (OntologyElement imp : imports) {
					String urlString = imp.getAttributes().get(RDF.resource);
					if (urlString != null) {
						System.out.println("Load ontology at URL: " + urlString);
						
						URL url = null;
						InputStream urlStream = null;
						try {
							url = new URL(urlString);
							urlStream = url.openStream();

							DocumentBuilder docBuilder =
									docBuilderFactory.newDocumentBuilder();
							Document importedXML = docBuilder.parse(urlStream);
							
							Map<String, Set<OntologyElement>> importedElements
									= parseAndLoadOntology(importedXML,
											parser);
							if (importedElements != null)
								mergeOntologies(elements, importedElements);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (ParserConfigurationException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						} catch (SAXException e) {
							e.printStackTrace();
						} finally {
							try {
								urlStream.close();
							} catch (IOException e) {
								
							}
						}
					}
				}
			}
		}
		
		return elements;
	}
	
	private void mergeOntologies(Map<String, Set<OntologyElement>> ontology1, Map<String, Set<OntologyElement>> ontology2) {
		for (String key : ontology2.keySet()) {
			if (ontology1.containsKey(key)) {
				Set<OntologyElement> set = ontology1.get(key);
				set.addAll(ontology2.get(key));
			}
			else {
				ontology1.put(key, ontology2.get(key));
			}
		}
	}
	
	public OntologyElement getElementWithName(String requestId,
			ITransaction transaction, String elementName) throws Exception {
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

	public boolean isSubtypeOf(String requestId, ITransaction transaction,
			String type, String supertype) throws Exception {
		if (type.equals(supertype))
			return true;
		
		OntologyElement typeElement = getElementWithName(requestId, transaction, type);
		Map<String, Set<OntologyElement>> subElements = typeElement.getSubElements();
		if (subElements.containsKey("rdfs:subClassOf")) {
			Set<OntologyElement> parentSet = subElements.get("rdfs:subClassOf");
			for (OntologyElement parent : parentSet) {
				if (isSubtypeOf(requestId, transaction, parent.getAttributes().get("rdf:ID"), supertype)) {
					return true;
				}
			}
		}
		
		return false;
	}
}
