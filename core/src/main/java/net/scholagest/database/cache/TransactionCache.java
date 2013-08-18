package net.scholagest.database.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.scholagest.database.commiter.DBAction;

public class TransactionCache {
    private List<DBAction> dbActionsLastToFirst;

    public TransactionCache() {
        this.dbActionsLastToFirst = new ArrayList<>();
    }

    public void addAction(DBAction dbAction) {
        dbActionsLastToFirst.add(0, dbAction);
    }

    public CacheResult get(String key, String column) {
        for (DBAction dbAction : dbActionsLastToFirst) {
            if (dbAction.getKey().equals(key) && dbAction.getColumn().equals(column)) {
                return new CacheResult(true, dbAction.getValue());
            }
        }

        return new CacheResult(false, null);
    }

    public Set<String> updateColumns(String key, Set<String> columns) {
        Iterator<String> columnIterator = columns.iterator();

        while (columnIterator.hasNext()) {
            String column = columnIterator.next();
            if (isDeleted(key, column)) {
                columnIterator.remove();
            }
        }

        for (String inserted : getInsertedColumns(key)) {
            if (!isDeleted(key, inserted)) {
                columns.add(inserted);
            }
        }

        return columns;
    }

    private boolean isDeleted(String key, String column) {
        CacheResult cacheResult = get(key, column);
        if (cacheResult.isFound() && cacheResult.getValue() == null) {
            return true;
        } else {
            return false;
        }
    }

    private HashSet<String> getInsertedColumns(String key) {
        HashSet<String> insertedColumns = new HashSet<>();

        for (DBAction dbAction : dbActionsLastToFirst) {
            if (dbAction.getKey().equals(key) && dbAction.getValue() != null) {
                insertedColumns.add(dbAction.getColumn());
            }
        }

        return insertedColumns;
    }
}
