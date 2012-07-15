package net.scholagest.database;

public interface IDatabase {
	/**
	 * Connect to the database.
	 */
    public void startup();
    
    /**
     * Close the connection to the database.
     */
    public void shutdown();
    
    public ITransaction getTransaction(String keyspaceName);
    
    public void createIndex(String columnName) throws DatabaseException;
}

