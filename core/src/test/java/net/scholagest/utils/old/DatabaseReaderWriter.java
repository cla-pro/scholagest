package net.scholagest.utils.old;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class DatabaseReaderWriter {
    private static final String ELEMENT_SEPARATOR = ":::";
    private static final String FILE_EXTENSION = ".sga";

    public void writeDataSetsInFile(String folderName, Map<String, Map<String, Map<String, Object>>> dataSets) {
        for (String dataSet : dataSets.keySet()) {
            try {
                BufferedWriter writer = openBufferedWriter(folderName, dataSet + FILE_EXTENSION);
                writeSetsInFile(writer, dataSets.get(dataSet));
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public Map<String, Map<String, Map<String, Object>>> readDataSetsFromResource(String folder, String[] dataSets) {
    	Map<String, Map<String, Map<String, Object>>> database = new HashMap<>();
    	
    	String folderPrefix = folder;
    	if (folderPrefix != null) {
    		folderPrefix += File.separatorChar;
    	}
    	
    	for (String dataSet : dataSets) {
    		String filePath = folderPrefix + dataSet + ".sga";
    		System.out.println(filePath);
    		InputStream resourceStream = ClassLoader.getSystemResourceAsStream(filePath);
    		BufferedReader reader;
            try {
                reader = new BufferedReader(new InputStreamReader(resourceStream));
                Map<String, Map<String, Object>> keyspaceData = readKeyspaceFromFile(reader);
                database.put(dataSet, keyspaceData);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
    	
        return database;
    }

    public Map<String, Map<String, Map<String, Object>>> readDataSetsFromFile(String folderName, String[] dataSets) {
        Map<String, Map<String, Map<String, Object>>> database = new HashMap<>();

        File folder = new File(folderName);
        if (!folder.exists()) {
            return database;
        }

        for (String dataSet : dataSets) {
            BufferedReader reader;
            try {
                reader = openBufferedReader(folderName, dataSet + FILE_EXTENSION);
                Map<String, Map<String, Object>> keyspaceData = readKeyspaceFromFile(reader);
                database.put(dataSet, keyspaceData);
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return database;
    }

    private void writeSetsInFile(BufferedWriter writer, Map<String, Map<String, Object>> data) throws IOException {
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
