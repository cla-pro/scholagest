package net.scholagest.old.managers.ontology;

import net.scholagest.old.database.Database;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.DefaultDatabaseConfiguration;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.namespace.CoreNamespace;

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
