package net.scholagest.database.commiter;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.hector.api.exceptions.HectorException;

public class DeleteDBAction extends KeyColumnDBAction {
    private String key;
    private String column;
    private final String originalValue;

    public DeleteDBAction(ColumnFamilyTemplate<String, String> template, String key, String column, String originalValue) {
        super(template);
        this.key = key;
        this.column = column;
        this.originalValue = originalValue;
    }

    @Override
    public void commit() throws HectorException {
        delete(key, column);
    }

    @Override
    public void rollback() throws HectorException {
        insert(key, column, originalValue);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public String getValue() {
        return null;
    }
}
