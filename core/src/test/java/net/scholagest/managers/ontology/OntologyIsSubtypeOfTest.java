package net.scholagest.managers.ontology;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.namespace.CoreNamespace;

public class OntologyIsSubtypeOfTest {
    public static void main(String[] args) throws Exception {
        IOntologyManager manager = new OntologyManager();

        Database database = new Database(new DefaultDatabaseConfiguration());
        database.startup();

        ITransaction transaction = database.getTransaction("ScholagestSecheron");
        try {
            manager.isSubtypeOf(CoreNamespace.tStudentPersonalInfo, CoreNamespace.tGroup);
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
}
