package net.scholagest.migration.updater;

import net.scholagest.initializer.jaxb.TSetProperty;
import net.scholagest.migration.RowsUpdater;
import net.scholagest.old.database.ITransaction;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SetPropertyRowsUpdater implements RowsUpdater {
    private static Logger LOG = LogManager.getLogger(SetPropertyRowsUpdater.class);

    private final TSetProperty setProperty;

    public SetPropertyRowsUpdater(TSetProperty setProperty) {
        this.setProperty = setProperty;
    }

    @Override
    public void updateAllRows() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();
        String key = setProperty.getKey();
        String propertyName = setProperty.getPropertyName();
        String value = setProperty.getValue();

        LOG.debug(String.format("[%s] Set property with name=%s on key=%s with value=%s", setProperty.getId(), key, propertyName, value));
        transaction.insert(key, propertyName, value, null);
    }
}
