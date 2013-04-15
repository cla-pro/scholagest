package net.scholagest.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
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
import net.scholagest.utils.DatabaseReaderWriter;
import net.scholagest.utils.InMemoryDatabase;
import net.scholagest.utils.InMemoryDatabase.InMemoryTransaction;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class OntologyHandlerTest {
    @Test
    public void testExtractImportURLWithImport() {
        String url1 = "file:///D:/Programming/eclipse-workspace/scholagest/core/src/test/resources/22-rdf-syntax-ns.rdfs";
        String url2 = "file:///D:/Programming/eclipse-workspace/scholagest/core/src/test/resources/rdf-schema.rdfs";

        OntologyHandler ontologyHandler = new OntologyHandler();
        try {
            Document scholagestTypeOntologyXmlDocument = loadXmlAsDocument("scholagest-types.rdfs");
            List<String> urls = ontologyHandler.extractImportURL(scholagestTypeOntologyXmlDocument);
            System.out.println(urls);
            assertEquals("3 imports should be found", 3, urls.size());
            assertTrue("Missing url: \"" + url1 + "\"", urls.contains(url1));
            assertTrue("Missing url: \"" + url2 + "\"", urls.contains(url2));
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
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringComments(true);
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File("src/test/resources/scholagest-properties.rdfs"));

        OntologyHandler handler = new OntologyHandler();

        Database database = new Database(new DefaultDatabaseConfiguration());
        database.startup();

        ITransaction transaction = database.getTransaction("ScholagestSecheron");
        try {
            Ontology ontology = handler.compileAndSaveOntology(UUID.randomUUID().toString(), transaction, doc);
            for (OntologyElement element : ontology.getAllOntologyElement()) {
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

    public static void main2(String[] args) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringComments(true);
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File("src/test/resources/scholagest-properties.rdfs"));

        OntologyHandler handler = new OntologyHandler();

        InMemoryDatabase database = new InMemoryDatabase();
        database.startup();

        InMemoryTransaction transaction = database.getTransaction("Ontology");
        try {
            Ontology ontology = handler.compileAndSaveOntology(UUID.randomUUID().toString(), transaction, doc);
            for (OntologyElement element : ontology.getAllOntologyElement()) {
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

        Map<String, Map<String, Map<String, Object>>> databaseContent = new HashMap<>();
        databaseContent.put(transaction.getKeyspace(), transaction.getValues());

        new DatabaseReaderWriter().writeDataSetsInFile("D:\\Programming\\eclipse-workspace\\Scholagest\\core\\src\\test\\resources\\data",
                databaseContent);

        database.shutdown();
    }

    public static void displayElement(String tab, OntologyElement element) {
        System.out.println(tab + "Type " + element.getType());
        System.out.println(tab + "Name " + element.getName());

        for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()) {
            System.out.println(tab + " --- " + attribute.getKey() + " => " + attribute.getValue());
        }

        String nextTab = tab + "    ";
        for (Map.Entry<String, Set<OntologyElement>> sub : element.getSubElements().entrySet()) {
            for (OntologyElement elem : sub.getValue()) {
                displayElement(nextTab, elem);
            }
        }
    }
}
