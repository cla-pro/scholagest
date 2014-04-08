package net.scholagest.old.managers.impl;

import net.scholagest.old.database.Database;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.database.DefaultDatabaseConfiguration;
import net.scholagest.old.database.IDatabase;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.IOntologyManager;
import net.scholagest.old.managers.IUserManager;
import net.scholagest.old.managers.impl.UserManager;
import net.scholagest.old.managers.ontology.OntologyManager;
import net.scholagest.old.services.impl.SecheronNamespace;
import net.scholagest.utils.ScholagestThreadLocal;

public class UserTest {
    public static void main(String[] args) throws DatabaseException {
        IOntologyManager ontologyManager = new OntologyManager();
        IUserManager userManager = new UserManager(ontologyManager);

        IDatabase database = new Database(new DefaultDatabaseConfiguration());

        ITransaction transaction = database.getTransaction(SecheronNamespace.SECHERON_KEYSPACE);
        ScholagestThreadLocal.setTransaction(transaction);
        try {
            userManager.createUser("admin", null);

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }
}
