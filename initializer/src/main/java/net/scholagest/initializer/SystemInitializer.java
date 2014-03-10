package net.scholagest.initializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.scholagest.old.business.ITeacherBusinessComponent;
import net.scholagest.old.business.IUserBusinessComponent;
import net.scholagest.old.business.impl.TeacherBusinessComponent;
import net.scholagest.old.business.impl.UserBusinessComponent;
import net.scholagest.old.database.Database;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.DefaultDatabaseConfiguration;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.IPageManager;
import net.scholagest.old.managers.impl.PageManager;
import net.scholagest.old.managers.impl.TeacherManager;
import net.scholagest.old.managers.impl.UserManager;
import net.scholagest.old.managers.ontology.Ontology;
import net.scholagest.old.managers.ontology.OntologyElement;
import net.scholagest.old.managers.ontology.OntologyHandler;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.namespace.AuthorizationRolesNamespace;
import net.scholagest.old.objects.TeacherObject;
import net.scholagest.old.objects.UserObject;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;

import com.google.inject.Inject;

public class SystemInitializer {
    private static Logger LOG = LogManager.getLogger(SystemInitializer.class.getName());

    private String keyspace;
    private String baseFolder;

    public static void main(String[] args) throws Exception {
        String baseFolder = "initializer/system/";
        if (args.length > 0) {
            baseFolder = args[0];
        }

        new SystemInitializer().setKeyspace("ScholagestSecheron").initialize(baseFolder);
    }

    @Inject
    public SystemInitializer() {}

    protected SystemInitializer setKeyspace(String keyspace) {
        this.keyspace = keyspace;
        return this;
    }

    public void initialize(String baseFolder) throws Exception {
        this.baseFolder = baseFolder;

        LOG.debug("Create connection to the database");
        DefaultDatabaseConfiguration databaseConfiguration = new DefaultDatabaseConfiguration();
        Database database = new Database(databaseConfiguration);

        LOG.info("Reset keyspace");
        DBReset.resetKeyspace(databaseConfiguration, keyspace);

        ITransaction transaction = database.getTransaction(keyspace);
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            fillDatabase(transaction);

            transaction.commit();
        } catch (Exception e) {
            LOG.info("Error while intializing the system.", e);
            try {
                transaction.rollback();
            } catch (DatabaseException e1) {
                LOG.info("Error while rolling back the transaction.", e1);
            }
        }

        database.shutdown();
    }

    protected void fillDatabase(ITransaction transaction) throws Exception {
        IOntologyManager ontologyManager = new OntologyManager();

        importOntology(transaction);
        importInitialData(ontologyManager);
    }

    private void importOntology(ITransaction transaction) throws Exception {
        LOG.info("Start importing the ontology");

        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setIgnoringComments(true);
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(getClass().getClassLoader().getResourceAsStream(baseFolder + "ontology/scholagest-properties.rdfs"));

        OntologyHandler handler = new OntologyHandler();

        Ontology ontology = handler.compileAndSaveOntology(UUID.randomUUID().toString(), transaction, doc);

        if (LOG.isDebugEnabled()) {
            printOntology(ontology);
        }

        LOG.info("End importing the ontology");
    }

    private void printOntology(Ontology ontology) {
        for (OntologyElement element : ontology.getAllOntologyElement()) {
            displayElement("", element);
        }
    }

    private void importInitialData(IOntologyManager ontologyManager) throws Exception {
        importPages(ontologyManager);
        importAdmins(ontologyManager);
    }

    private void importPages(IOntologyManager ontologyManager) throws Exception {
        LOG.info("Start importing pages");

        IPageManager pageManager = new PageManager(ontologyManager);

        List<List<String>> pages = readFile(baseFolder + "pages.sga");

        for (List<String> page : pages) {
            LOG.debug("Insert page: " + page.get(0) + ";" + page.get(1) + ";" + page.get(2));
            pageManager.createPage(page.get(0), page.get(1), new HashSet<String>(Arrays.asList(page.get(2).split(","))));
        }

        LOG.info("End importing pages");
    }

    private void importAdmins(IOntologyManager ontologyManager) throws Exception {
        LOG.info("Start importing administrators");

        TeacherManager teacherManager = new TeacherManager(ontologyManager);
        ITeacherBusinessComponent teacherBusinessComponent = new TeacherBusinessComponent(teacherManager);
        IUserBusinessComponent userBusinessComponent = new UserBusinessComponent(new UserManager(ontologyManager), teacherManager);

        List<List<String>> users = readFile(baseFolder + "admins.sga");

        for (List<String> user : users) {
            String firstName = user.get(0);
            String lastName = user.get(1);
            String password = user.get(2);
            String role = user.get(3);

            LOG.info("Insert admin: \"" + firstName + "\" \"" + lastName + "\";" + password);
            Map<String, Object> teacherProperties = createTeacherProperties(firstName, lastName);
            TeacherObject teacher = teacherBusinessComponent.createTeacher(role, teacherProperties);

            UserObject userObject = userBusinessComponent.createUser(teacher.getKey());
            userObject.getRoles().add(AuthorizationRolesNamespace.ROLE_ADMIN);
            userObject.getPermissions().add(teacher.getKey());

            userBusinessComponent.setPassword(teacher.getKey(), password);

            addRoles(userObject, user.get(3).split("::"));
        }

        LOG.info("End importing administrators");
    }

    private Map<String, Object> createTeacherProperties(String firstName, String lastName) {
        Map<String, Object> teacherProperties = new HashMap<>();

        teacherProperties.put("pTeacherFirstName", firstName);
        teacherProperties.put("pTeacherLastName", lastName);

        return teacherProperties;
    }

    private void addRoles(UserObject userObject, String[] roleList) throws DatabaseException {
        DBSet rolesSet = userObject.getRoles();

        for (String role : roleList) {
            LOG.debug(" --- Add role: " + role);
            rolesSet.add(role);
        }
    }

    protected List<List<String>> readFile(String filename) throws IOException {
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
        LOG.debug(tab + "Type " + element.getType());
        LOG.debug(tab + "Name " + element.getName());

        for (Map.Entry<String, String> attribute : element.getAttributes().entrySet()) {
            LOG.debug(tab + " --- " + attribute.getKey() + " => " + attribute.getValue());
        }

        String nextTab = tab + "    ";
        for (Map.Entry<String, Set<OntologyElement>> sub : element.getSubElements().entrySet()) {
            for (OntologyElement elem : sub.getValue()) {
                displayElement(nextTab, elem);
            }
        }
    }

    protected String getBaseFolder() {
        return baseFolder;
    }
}
