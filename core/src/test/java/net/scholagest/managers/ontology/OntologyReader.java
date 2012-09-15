package net.scholagest.managers.ontology;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.CoreNamespace;

public class OntologyReader {
    public static void main(String[] args) {
        OntologyManager manager = new OntologyManager();

        Database database = new Database(new DefaultDatabaseConfiguration());
        database.startup();

        ITransaction transaction = database.getTransaction("ScholagestSecheron");
        try {
            // System.out.println("IsSubtypeOf: " +
            // manager.isSubtypeOf(UUID.randomUUID().toString(), transaction,
            // CoreNamespace.tStudentPersonalInfo, CoreNamespace.tGroup));
            // System.out.println("IsSubtypeOf: " +
            // manager.isSubtypeOf(UUID.randomUUID().toString(), transaction,
            // CoreNamespace.tStudent, CoreNamespace.tGroup));

            Set<String> propertySet = new HashSet<>();
            propertySet.add("abcd");
            propertySet.add("pStudentFirstName");
            propertySet.add("pStudentLastName");
            propertySet.add("pTeacherFirstName");
            Set<String> filtered = manager.filterPropertiesWithCorrectDomain(UUID.randomUUID().toString(), transaction,
                    CoreNamespace.tStudentPersonalInfo, propertySet);

            System.out.println(filtered);

            // for (String ontologyElementName :
            // transaction.getColumns(CoreNamespace.ontologyBase)) {
            // System.out.println(ontologyElementName);
            // System.out.println(transaction.get(ontologyElementName, RDF.type,
            // null));
            // readAndPrintElement(transaction, ontologyElementName);
            // }
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

    public static void readAndPrintElement(ITransaction transaction, String key) throws Exception {
        for (String attribute : transaction.getColumns(key)) {
            System.out.println("    " + key + " -> " + attribute + " -> " + transaction.get(key, attribute, null));
        }
    }
}
