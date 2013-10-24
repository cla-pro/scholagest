package net.scholagest.migration.updater;

import net.scholagest.database.ITransaction;
import net.scholagest.initializer.jaxb.TDeleteNode;
import net.scholagest.migration.RowsUpdater;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteNodeRowsUpdater implements RowsUpdater {
    private static Logger LOG = LogManager.getLogger(DeleteNodeRowsUpdater.class);

    private final TDeleteNode deleteNode;

    public DeleteNodeRowsUpdater(TDeleteNode deleteNode) {
        this.deleteNode = deleteNode;
    }

    @Override
    public void updateAllRows() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();
        String key = deleteNode.getKey();

        LOG.debug(String.format("[%s] Delete node with key=%s", deleteNode.getId(), key));
        for (String column : transaction.getColumns(key)) {
            transaction.delete(key, column, null);
        }
    }
}
