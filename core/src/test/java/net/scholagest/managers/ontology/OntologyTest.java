package net.scholagest.managers.ontology;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.managers.ontology.parser.OntologyElement;

import org.w3c.dom.Document;

public class OntologyTest {
	public static void main(String[] args) throws Exception {
		DocumentBuilderFactory docBuilderFactory =
				DocumentBuilderFactory.newInstance();
		docBuilderFactory.setIgnoringComments(true);
		docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(
        		new File("src/test/resources/scholagest.rdfs"));
        
        OntologyManager manager = new OntologyManager();
        
        Database database = new Database(new DefaultDatabaseConfiguration());
		database.startup();
		
		ITransaction transaction = database.getTransaction("ScholagestSecheron");
		try {
	        Map<String, Set<OntologyElement>> elements =
	        		manager.compileOntology(UUID.randomUUID().toString(),
	        				transaction, doc);
	        for (Map.Entry<String, Set<OntologyElement>> entry
	        		: elements.entrySet()) {
	        	System.out.println("=================== " + entry.getKey());
	        	
	        	for (OntologyElement element : entry.getValue()) {
	        		displayElement("", element);
	        	}
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
		System.out.println(tab + element.getType());
		System.out.println(tab + element.getName());
		
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
