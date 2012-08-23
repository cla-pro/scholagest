package net.scholagest.managers.ontology;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
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
				= this.parseAndLoadOntology(xmlOntology, parser);
		
		//TODO Solve the names.
		
		
		//TODO Store into the database.
		new OntologySaver().saveOntology(requestId, transaction, elements);
		
		return elements;
	}
	
	private Map<String, Set<OntologyElement>> parseAndLoadOntology(
			Document xmlOntology, OntologyParser parser) {
		Map<String, Set<OntologyElement>> elements
				= parser.parseOntology(xmlOntology);
		
		//Search for imports.
		Set<OntologyElement> owlOntologies = elements.get(OWL.ontology);
		if (owlOntologies != null && owlOntologies.size() > 0) {
			for (OntologyElement owlOnto : owlOntologies) {
				Set<OntologyElement> imports =
						owlOnto.getSubElements().get(OWL.imports);
				
				if (imports == null)
					continue;
				
				for (OntologyElement imp : imports) {
					String urlString = imp.getAttributes().get("rdf:resource");
					if (urlString != null) {
						System.out.println("Load ontology at URL: " + urlString);
						
						URL url = null;
						InputStream urlStream = null;
						try {
							url = new URL(urlString);
							urlStream = url.openStream();

							DocumentBuilder docBuilder =
									this.docBuilderFactory.newDocumentBuilder();
							Document importedXML = docBuilder.parse(urlStream);
							
							Map<String, Set<OntologyElement>> importedElements
									= this.parseAndLoadOntology(importedXML,
											parser);
							if (importedElements != null)
								elements.putAll(importedElements);
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

	public List<String> getTypeHierarchy(String requestId,
			ITransaction transaction, String type) throws Exception {
		OntologyElement typeElement = getElementWithName(requestId, transaction, type);
		Map<String, Set<OntologyElement>> subElements = typeElement.getSubElements();
		if (subElements.containsKey("rdfs:subClassOf")) {
			Set<OntologyElement> parents = subElements.get("rdfs:subClassOf");
			for (OntologyElement)
		}
		
		return null;
	}
	
	private List<String> extractTypeHierarchy(Ontology)
}
