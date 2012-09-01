package net.scholagest.managers.ontology;

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
			for (String ontologyElementName : transaction.getColumns(CoreNamespace.ontologyBase)) {
				System.out.println(ontologyElementName);
			}
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
