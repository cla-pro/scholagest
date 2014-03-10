package net.scholagest.old.database.commiter;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.hector.api.exceptions.HectorException;

public class InsertDBAction extends KeyColumnDBAction {
    private String key;
    private String column;
    private String value;
    private final String originalValue;

    public InsertDBAction(ColumnFamilyTemplate<String, String> template, String key, String column, String value, String originalValue) {
        super(template);
        this.key = key;
        this.column = column;
        this.value = value;
        this.originalValue = originalValue;
    }

    @Override
    public void commit() throws HectorException {
        insert(key, column, value);
    }

    @Override
    public void rollback() throws HectorException {
        if (originalValue == null) {
            delete(key, column);
        } else {
            insert(key, column, originalValue);
        }
    }

    public String getKey() {
        return key;
    }

    public String getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }
}
