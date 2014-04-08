package net.scholagest.initializer;

import java.util.Iterator;
import java.util.UUID;

import net.scholagest.old.database.Database;
import net.scholagest.old.database.DbRow;
import net.scholagest.old.database.DefaultDatabaseConfiguration;
import net.scholagest.old.database.ITransaction;
import net.scholagest.utils.ScholagestThreadLocal;

public class GetAllRowsTest {
    public static void main(String[] args) {
        Database database = new Database(new DefaultDatabaseConfiguration());

        ITransaction transaction = database.getTransaction("ScholagestSecheron");
        ScholagestThreadLocal.setTransaction(transaction);
        ScholagestThreadLocal.setRequestId(UUID.randomUUID().toString());

        Iterator<DbRow> allRowsIterator = transaction.getAllRows();
        while (allRowsIterator.hasNext()) {
            DbRow dbRow = allRowsIterator.next();
            System.out.println("Key: " + dbRow.getKey() + " Columns: " + dbRow.getColumns());
        }

        transaction.commit();
    }
}
