package net.scholagest.managers.impl;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.IUserManager;
import net.scholagest.managers.ontology.OntologyManager;
import net.scholagest.services.impl.SecheronNamespace;
import net.scholagest.utils.ScholagestThreadLocal;

public class UserTest {
    public static void main(String[] args) throws DatabaseException {
        OntologyManager ontologyManager = new OntologyManager();
        IUserManager userManager = new UserManager(ontologyManager);

        IDatabase database = new Database(new DefaultDatabaseConfiguration());

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            userManager.createUser("admin", "admin");

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }
}
