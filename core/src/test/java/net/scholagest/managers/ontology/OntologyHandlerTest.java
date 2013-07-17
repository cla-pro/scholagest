package net.scholagest.managers.ontology;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class OntologyHandlerTest {
    @Test
    public void testExtractImportURLWithImport() throws Exception {
        String url1 = "file:///D:/Programming/eclipse-workspace/scholagest/core/src/test/resources/22-rdf-syntax-ns.rdfs";
        String url2 = "file:///D:/Programming/eclipse-workspace/scholagest/core/src/test/resources/rdf-schema.rdfs";

        OntologyHandler ontologyHandler = new OntologyHandler();

        Document scholagestTypeOntologyXmlDocument = loadXmlAsDocument("scholagest-types.rdfs");
        List<String> urls = ontologyHandler.extractImportURL(scholagestTypeOntologyXmlDocument);
        System.out.println(urls);
        assertEquals("3 imports should be found", 3, urls.size());
        assertTrue("Missing url: \"" + url1 + "\"", urls.contains(url1));
        assertTrue("Missing url: \"" + url2 + "\"", urls.contains(url2));
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
}
