package net.scholagest.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.parser.OntologyElement;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class OntologyHandlerTest {

	@Test
	public void testExtractImportURLWithImport() {
		OntologyHandler ontologyHandler = new OntologyHandler();
		try {
			Document scholagestTypeOntologyXmlDocument = loadXmlAsDocument("scholagest-types.rdfs");
			List<String> urls = ontologyHandler.extractImportURL(scholagestTypeOntologyXmlDocument);
			assertEquals("3 imports should be found", 2, urls.size());
			assertTrue("Missing url: \"http://www.w3.org/1999/02/22-rdf-syntax-ns\"", urls.contains("http://www.w3.org/1999/02/22-rdf-syntax-ns"));
			assertTrue("Missing url: \"http://www.w3.org/2000/01/rdf-schema\"", urls.contains("http://www.w3.org/2000/01/rdf-schema"));
		} catch (Exception e) {
			e.printStackTrace();
			fail("No exception expected");
		}
	}

	@Test
	public void testExtractImportURLWithoutImport() {
		OntologyHandler ontologyHandler = new OntologyHandler();
		try {
			Document rdfOntologyXmlDocument = loadXmlAsDocument("rdf.xml");
			List<String> urls = ontologyHandler.extractImportURL(rdfOntologyXmlDocument);
			assertEquals("No imports should be found", 0, urls.size());
		} catch (Exception e) {
			e.printStackTrace();
			fail("No exception expected");
		}
	}
	
	private Document loadXmlAsDocument(String fileName) throws Exception {
		InputStream xmlStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		return parser.parse(new InputSource(xmlStream));
	}

	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory docBuilderFactory =
				DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringComments(true);
		docBuilderFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(
				new File("src/test/resources/scholagest-properties.rdfs"));

		OntologyHandler handler = new OntologyHandler();

		Database database = new Database(new DefaultDatabaseConfiguration());
		database.startup();

		ITransaction transaction = database.getTransaction("ScholagestSecheron");
		try {
			Ontology ontology =
					handler.compileAndSaveOntology(UUID.randomUUID().toString(),
							transaction, doc);
			for (OntologyElement element
					: ontology.getAllOntologyElement()) {
				displayElement("", element);
			}

			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				transaction.rollback();
			} catch (DatabaseException e1) {
				e1.printStackTrace();
			}
		}

		database.shutdown();
	}

	public static void displayElement(String tab, OntologyElement element) {
		System.out.println(tab + "Type " + element.getType());
		System.out.println(tab + "Name " + element.getName());

		for (Map.Entry<String, String> attribute
				: element.getAttributes().entrySet()) {
			System.out.println(tab + " --- " + attribute.getKey() + " => "
					+ attribute.getValue());
		}

		String nextTab = tab + "    ";
		for (Map.Entry<String, Set<OntologyElement>> sub
				: element.getSubElements().entrySet()) {
			for (OntologyElement elem : sub.getValue()) {
				displayElement(nextTab, elem);
			}
		}
	}
}
