package net.scholagest.database;

import java.util.Map;

public class DbRow {
    private String key;
    private Map<String, Object> columns;

    public DbRow(String key, Map<String, Object> columns) {
        this.key = key;
        this.columns = columns;
    }

    public String getKey() {
        return key;
    }

    public Map<String, Object> getColumns() {
        return columns;
    }
}
