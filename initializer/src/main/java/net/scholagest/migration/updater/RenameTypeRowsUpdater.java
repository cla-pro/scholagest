package net.scholagest.migration.updater;

import net.scholagest.initializer.jaxb.TRenameType;
import net.scholagest.old.database.DbRow;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RenameTypeRowsUpdater extends AbstractRowsUpdater {
    private static Logger LOG = LogManager.getLogger(RenameTypeRowsUpdater.class);

    private final TRenameType renameType;

    public RenameTypeRowsUpdater(TRenameType renameType) {
        this.renameType = renameType;
    }

    @Override
    protected void updateRowIfNecessary(DbRow row) {
        if (isTypeMatching(row, renameType.getName())) {
            String newTypeName = renameType.getNewName();
            LOG.debug(String.format("[%s] Rename type from=%s to=%s", renameType.getId(), renameType.getName(), newTypeName));
            ScholagestThreadLocal.getTransaction().insert(row.getKey(), RDF.type, newTypeName, null);
        }
    }
}
