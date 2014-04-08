package net.scholagest.migration.updater;

import java.util.Iterator;

import net.scholagest.migration.RowsUpdater;
import net.scholagest.old.database.DbRow;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.utils.ScholagestThreadLocal;

public abstract class AbstractRowsUpdater implements RowsUpdater {

    protected boolean isTypeMatching(DbRow row, String typeName) {
        String type = (String) row.getColumns().get(RDF.type);

        return typeName.equals(type);
    }

    @Override
    public void updateAllRows() {
        Iterator<DbRow> rows = ScholagestThreadLocal.getTransaction().getAllRows();
        while (rows.hasNext()) {
            DbRow row = rows.next();

            updateRowIfNecessary(row);
        }
    }

    protected abstract void updateRowIfNecessary(DbRow row);
}
