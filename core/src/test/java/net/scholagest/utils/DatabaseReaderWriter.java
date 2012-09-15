package net.scholagest.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseReaderWriter {
    private static final String ELEMENT_SEPARATOR = ":::";
    private static final String FILE_EXTENSION = ".sga";

    public void writeDatabaseInFile(String folderName, Map<String, Map<String, Map<String, Object>>> data) {
        for (String keyspace : data.keySet()) {
            try {
                BufferedWriter writer = openBufferedWriter(folderName, keyspace + FILE_EXTENSION);
                writeKeyspaceInFile(writer, data.get(keyspace));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Map<String, Map<String, Object>>> readDatabaseFromFile(String folderName, String[] keyspaces) {
        Map<String, Map<String, Map<String, Object>>> database = new HashMap<>();

        File folder = new File(folderName);
        if (!folder.exists()) {
            return database;
        }

        for (String keyspace : keyspaces) {
            BufferedReader reader;
            try {
                reader = openBufferedReader(folderName, keyspace + FILE_EXTENSION);
                Map<String, Map<String, Object>> keyspaceData = readKeyspaceFromFile(reader);
                database.put(keyspace, keyspaceData);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return database;
    }

    private void writeKeyspaceInFile(BufferedWriter writer, Map<String, Map<String, Object>> data) throws IOException {
        for (String key : data.keySet()) {
            Map<String, Object> columns = data.get(key);
            for (String columnName : columns.keySet()) {
                writer.write(key + ELEMENT_SEPARATOR + columnName + ELEMENT_SEPARATOR + columns.get(columnName) + "\n");
            }
        }
    }

    private Map<String, Map<String, Object>> readKeyspaceFromFile(BufferedReader reader) throws IOException {
        Map<String, Map<String, Object>> data = new HashMap<>();

        String line = reader.readLine();
        while (line != null) {
            String[] elements = line.split(ELEMENT_SEPARATOR);

            String key = elements[0];
            String column = elements[1];
            String value = elements[2];

            Map<String, Object> columns = data.get(key);
            if (columns == null) {
                columns = new HashMap<>();
                data.put(key, columns);
            }

            columns.put(column, value);

            line = reader.readLine();
        }

        return data;
    }

    private BufferedWriter openBufferedWriter(String folderName, String fileName) throws IOException {
        File folder = new File(folderName);

        if (!folder.exists()) {
            folder.mkdirs();
        }

        String fullPath = folderName + File.separatorChar + fileName;

        return new BufferedWriter(new FileWriter(new File(fullPath)));
    }

    private BufferedReader openBufferedReader(String folderName, String fileName) throws IOException {
        String fullPath = folderName + File.separatorChar + fileName;

        return new BufferedReader(new FileReader(new File(fullPath)));
    }
}
