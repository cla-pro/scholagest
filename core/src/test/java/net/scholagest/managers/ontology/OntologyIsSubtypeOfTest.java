package net.scholagest.managers.ontology;

import java.util.UUID;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.managers.impl.CoreNamespace;

public class OntologyIsSubtypeOfTest {
	public static void main(String[] args) throws Exception {
        OntologyManager manager = new OntologyManager();
        
        Database database = new Database(new DefaultDatabaseConfiguration());
		database.startup();
		
		ITransaction transaction = database.getTransaction("ScholagestSecheron");
		try {
			manager.isSubtypeOf(UUID.randomUUID().toString(), transaction, CoreNamespace.tStudentPersonalInfo, CoreNamespace.tGroup);
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
