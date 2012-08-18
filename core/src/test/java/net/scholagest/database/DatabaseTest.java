package net.scholagest.database;

import net.scholagest.database.Database;
import net.scholagest.database.DatabaseException;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;

public class DatabaseTest {
	public static void main(String[] args) {
		Database database = new Database(new DefaultDatabaseConfiguration());
		database.startup();
		
		String key1 = "key1";
		String column1 = "column1";
		String value1 = "value1";
		
		ITransaction transaction =
				database.getTransaction("scholagestKeyspace");
		try {
			transaction.insert(key1, column1, value1, null);
			
			String result = (String) transaction.get(key1, column1, null);
			System.out.println("Result: " + result);
			
			for (String col : transaction.getColumns(key1)) {
				System.out.println("Column: " + col);
			}
			
			transaction.delete(key1, column1, null);

			result = (String) transaction.get(key1, column1, null);
			System.out.println("Result: " + result);
			
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
}
