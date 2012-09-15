package net.scholagest.managers.ontology;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OntologyHandler {
	private static Logger LOG = Logger.getLogger(OntologyHandler.class);
	
	public Ontology compileAndSaveOntology(String requestId,
			ITransaction transaction, Document xmlOntology) throws Exception {
		Set<Document> allOntologyAsXmlSet = extractAndDownloadsImport(xmlOntology, new HashSet<String>());
		allOntologyAsXmlSet.add(xmlOntology);
		
		Ontology globalOntology = new Ontology();
		for (Document document : allOntologyAsXmlSet) {
			Ontology ontology = compileXMLOntology(document);
			copyOntologyIntoOntology(ontology, globalOntology);
		}
		
		new OntologySaver().saveOntology(requestId, transaction, globalOntology);
		
		return globalOntology;
	}
	
	private void copyOntologyIntoOntology(Ontology src, Ontology dst) {
		dst.addAllOntologyElement(src.getAllOntologyElement());
	}
	
	private Ontology compileXMLOntology(Document xmlOntology) {
		Ontology ontology = new Ontology();
		
		OntologyParser parser = new OntologyParser();
		Map<String, Set<OntologyElement>> elements = parser.parseOntology(xmlOntology);
		for (Set<OntologyElement> ontologyElementSet : elements.values()) {
			ontology.addAllOntologyElement(ontologyElementSet);
		}
		
		return ontology;
	}

	private Set<Document> extractAndDownloadsImport(Document xmlOntology, Set<String> alreadyImported) throws Exception {
		Set<Document> importSet = new HashSet<>();
		List<String> urlList = extractImportURL(xmlOntology);
		
		for (String urlString : urlList) {
			if (!alreadyImported.contains(urlString)) {
				Document imported = downloadImport(urlString);
				if (imported != null) {
					importSet.add(imported);
					alreadyImported.add(urlString);
					
					importSet.addAll(extractAndDownloadsImport(imported, alreadyImported));
				}
			}
		}
		
		return importSet;
	}
	
	List<String> extractImportURL(Document xmlOntology) throws Exception {
		List<String> importList = new ArrayList<>();
		
		NodeList ontologyNodeList = xmlOntology.getElementsByTagName("owl:imports");
		
		for (int i = 0; i < ontologyNodeList.getLength(); i++) {
			Node ontologyNode = ontologyNodeList.item(i);
			importList.add(ontologyNode.getAttributes().getNamedItem("rdf:resource").getNodeValue());
		}
		
		return importList;
	}
	
	private Document downloadImport(String urlString) {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringComments(true);
		docBuilderFactory.setNamespaceAware(true);
		
		URL url = null;
		InputStream urlStream = null;
		try {
			url = new URL(urlString);
			urlStream = url.openStream();

			DocumentBuilder docBuilder =
					docBuilderFactory.newDocumentBuilder();
			return docBuilder.parse(urlStream);
		} catch (MalformedURLException e) {
			LOG.error("Error while loading imported ontology - wrong URL", e);
		} catch (ParserConfigurationException e) {
			LOG.error("Error while loading imported ontology", e);
		} catch (IOException e) {
			LOG.error("Error while loading imported ontology", e);
		} catch (SAXException e) {
			LOG.error("Error while parsing the imported XML ontology", e);
		} finally {
			try {
				urlStream.close();
			} catch (IOException e) {
				LOG.warn("Error while closing stream", e);
			}
		}
		
		return null;
	}
}
