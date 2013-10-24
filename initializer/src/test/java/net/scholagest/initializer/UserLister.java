package net.scholagest.initializer;

import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.ITransaction;
import net.scholagest.namespace.CoreNamespace;

public class UserLister {

    public static void main(String[] args) {
        new UserLister().run();
    }

    private void run() {
        Database database = new Database(new DefaultDatabaseConfiguration());

        ITransaction transaction = database.getTransaction("ScholagestSecheron");

        try {
            for (String userKey : transaction.getColumns(CoreNamespace.userBase)) {
                System.out.println(userKey);
            }

            System.out.println(transaction.get(CoreNamespace.userBase, "clavanchy", null));

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.rollback();
        }
    }
}
