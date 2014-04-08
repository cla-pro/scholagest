package net.scholagest.migration.updater;

import net.scholagest.initializer.jaxb.TCreateNode;
import net.scholagest.migration.RowsUpdater;
import net.scholagest.old.database.ITransaction;
import net.scholagest.old.managers.ontology.RDF;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CreateNodeRowsUpdater implements RowsUpdater {
    private static Logger LOG = LogManager.getLogger(CreateNodeRowsUpdater.class);

    private final TCreateNode createNode;

    public CreateNodeRowsUpdater(TCreateNode createNode) {
        this.createNode = createNode;
    }

    @Override
    public void updateAllRows() {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String key = createNode.getKey();
        String type = createNode.getType();

        LOG.debug(String.format("[%s] Create node with key=%s type=%s", createNode.getId(), key, type));
        transaction.insert(key, RDF.type, type, null);
    }
}
