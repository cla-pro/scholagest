package net.scholagest.migration.updater;

import net.scholagest.database.DbRow;
import net.scholagest.database.ITransaction;
import net.scholagest.initializer.jaxb.TCreateProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreatePropertyRowsUpdater extends AbstractRowsUpdater {
    private static Logger LOG = LogManager.getLogger(CreatePropertyRowsUpdater.class);

    private TCreateProperty createProperty;

    public CreatePropertyRowsUpdater(TCreateProperty createProperty) {
        this.createProperty = createProperty;
    }

    @Override
    protected void updateRowIfNecessary(DbRow row) {
        if (isTypeMatching(row, createProperty.getTypeName())) {
            String key = row.getKey();
            String defaultValue = createProperty.getDefaultValue();
            String propertyName = createProperty.getName();

            if (defaultValue != null) {
                LOG.debug(String.format("[%s] Create property name=%s type=%s applyOnType=%s defaultValue=%s on key=%s", createProperty.getId(),
                        propertyName, createProperty.getTypeName(), createProperty.getApplyOnType(), defaultValue, key));

                ITransaction transaction = ScholagestThreadLocal.getTransaction();
                transaction.insert(key, propertyName, defaultValue, null);
            }
        }
    }
}
