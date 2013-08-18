package net.scholagest.database.commiter;

import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;

public abstract class KeyColumnDBAction implements DBAction {
    private ColumnFamilyTemplate<String, String> template;

    protected KeyColumnDBAction(ColumnFamilyTemplate<String, String> template) {
        this.template = template;
    }

    protected void insert(String key, String column, String value) {
        ColumnFamilyUpdater<String, String> updater = template.createUpdater(key);
        updater.setString(column, value);
        template.update(updater);
    }

    protected void delete(String key, String column) {
        template.deleteColumn(key, column);
    }
}
