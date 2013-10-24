package net.scholagest.migration.updater;

import net.scholagest.database.DbRow;
import net.scholagest.initializer.jaxb.TRemoveProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RemovePropertyRowsUpdater extends AbstractRowsUpdater {
    private static Logger LOG = LogManager.getLogger(RemovePropertyRowsUpdater.class);

    private final TRemoveProperty removeProperty;

    public RemovePropertyRowsUpdater(TRemoveProperty removeProperty) {
        this.removeProperty = removeProperty;
    }

    @Override
    protected void updateRowIfNecessary(DbRow row) {
        if (isTypeMatching(row, removeProperty.getTypeName())) {
            String propertyName = removeProperty.getName();
            Object value = row.getColumns().get(propertyName);

            // TODO Is it necessary to check the value?
            if (value != null) {
                String key = row.getKey();
                LOG.debug(String.format("[%s] Remove property with name=\"%s\" on key=\"%s\"", removeProperty.getId(), propertyName, key));
                ScholagestThreadLocal.getTransaction().delete(key, propertyName, null);
            }
        }
    }
}
