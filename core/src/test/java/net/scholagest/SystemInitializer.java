package net.scholagest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
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
import net.scholagest.managers.IPageManager;
import net.scholagest.managers.IUserManager;
import net.scholagest.managers.impl.PageManager;
import net.scholagest.managers.impl.UserManager;
import net.scholagest.managers.ontology.Ontology;
import net.scholagest.managers.ontology.OntologyElement;
import net.scholagest.managers.ontology.OntologyHandler;
import net.scholagest.managers.ontology.OntologyManager;

import org.w3c.dom.Document;

public class SystemInitializer {
    public static void main(String[] args) throws Exception {
        new SystemInitializer().initialize();
    }

    public void initialize() throws Exception {
        Database database = new Database(new DefaultDatabaseConfiguration());

        ITransaction transaction = database.getTransaction("ScholagestSecheron");
        try {
            OntologyManager ontologyManager = new OntologyManager();

            importOntology(transaction);
            importInitialData(transaction, ontologyManager);

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

    private void importOntology(ITransaction transaction) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringComments(true);
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File("src/test/resources/scholagest-properties.rdfs"));

        OntologyHandler handler = new OntologyHandler();

        Ontology ontology = handler.compileAndSaveOntology(UUID.randomUUID().toString(), transaction, doc);
        for (OntologyElement element : ontology.getAllOntologyElement()) {
            displayElement("", element);
        }
    }

    private void importInitialData(ITransaction transaction, OntologyManager ontologyManager) throws Exception {
        importPages(transaction, ontologyManager);
        importUsers(transaction, ontologyManager);
    }

    private void importPages(ITransaction transaction, OntologyManager ontologyManager) throws Exception {
        IPageManager pageManager = new PageManager(ontologyManager);

        List<List<String>> pages = readFile("pages.sga");

        for (List<String> page : pages) {
            System.out.println("Insert page: " + page.get(0) + ";" + page.get(1) + ";" + page.get(2));
            pageManager.createPage(UUID.randomUUID().toString(), transaction, page.get(0), page.get(1),
                    new HashSet<String>(Arrays.asList(page.get(2).split(","))));
        }
    }

    private void importUsers(ITransaction transaction, OntologyManager ontologyManager) throws Exception {
        IUserManager userManager = new UserManager(ontologyManager);

        List<List<String>> users = readFile("users.sga");

        for (List<String> user : users) {
            System.out.println("Insert user: " + user.get(0) + ";" + user.get(1));
            userManager.createUser(UUID.randomUUID().toString(), transaction, user.get(0), user.get(1));
        }
    }

    private List<List<String>> readFile(String filename) throws IOException {
        BufferedReader br = null;
        List<List<String>> file = new ArrayList<>();

        try {
            br = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(filename)));
            String line;
            while ((line = br.readLine()) != null) {
                String[] elements = line.split(";");
                file.add(Arrays.asList(elements));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
        }

        return file;
    }

    private void displayElement(String tab, OntologyElement element) {
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
