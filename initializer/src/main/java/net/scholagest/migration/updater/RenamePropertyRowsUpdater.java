package net.scholagest.migration.updater;

import net.scholagest.database.DbRow;
import net.scholagest.initializer.jaxb.TRenameProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RenamePropertyRowsUpdater extends AbstractRowsUpdater {
    private static Logger LOG = LogManager.getLogger(RenamePropertyRowsUpdater.class);

    private final TRenameProperty renameProperty;

    public RenamePropertyRowsUpdater(TRenameProperty renameProperty) {
        this.renameProperty = renameProperty;
    }

    @Override
    protected void updateRowIfNecessary(DbRow row) {
        if (isTypeMatching(row, renameProperty.getTypeName())) {
            String propertyName = renameProperty.getName();
            String key = row.getKey();
            String newPropertyName = renameProperty.getNewName();
            Object value = row.getColumns().get(propertyName);

            if (value != null) {
                LOG.debug(String.format("[%s] Rename property for key=%s with from=%s to=%s", renameProperty.getId(), key, propertyName,
                        newPropertyName));
                ScholagestThreadLocal.getTransaction().delete(key, propertyName, null);
                ScholagestThreadLocal.getTransaction().insert(key, newPropertyName, value, null);
            }
        }
    }
}
