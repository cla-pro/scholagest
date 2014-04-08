package net.scholagest.db.entity;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestPostgre {
    public static void main(final String[] args) throws ClassNotFoundException, SQLException, IOException {
        Class.forName("org.postgresql.Driver");
        final String url = "jdbc:postgresql://localhost/scholagest";
        final Connection conn = DriverManager.getConnection(url, "scholagest_dba", "scholagestdba");

        try {
            // playScript(conn,
            // "src/main/resources/liquibase-0.15.0-SNAPSHOT.sql");

            final DatabaseMetaData metaData = conn.getMetaData();
            final ResultSet tables = metaData.getTables(null, "public", "%", null);
            while (tables.next()) {
                System.out.println(tables.getString(3));
            }
        } finally {
            conn.close();
        }
    }

    private static void playScript(final Connection conn, final String fileName) throws IOException, SQLException {
        final BufferedReader reader = new BufferedReader(new FileReader(fileName));

        try {
            String line = reader.readLine();
            while (line != null) {
                if (!line.isEmpty() && !line.startsWith("--")) {
                    final String query = removeSemiColumn(line);
                    System.out.println("Play query: \"" + query + "\"");

                    final Statement statement = conn.createStatement();
                    statement.execute(query);
                }

                line = reader.readLine();
            }
        } finally {
            reader.close();
        }
    }

    private static String removeSemiColumn(final String line) {
        if (line.endsWith(";")) {
            return line.substring(0, line.length() - 1);
        } else {
            return line;
        }
    }
}
