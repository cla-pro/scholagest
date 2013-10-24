package net.scholagest.migration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import net.scholagest.database.Database;
import net.scholagest.database.DefaultDatabaseConfiguration;
import net.scholagest.database.IDatabase;
import net.scholagest.database.ITransaction;
import net.scholagest.initializer.jaxb.TChangeSet;
import net.scholagest.initializer.jaxb.TDbUpdate;
import net.scholagest.migration.updater.UpdaterFactory;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.utils.ConfigurationService;
import net.scholagest.utils.ScholagestProperty;
import net.scholagest.utils.ScholagestThreadLocal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Migrator {
    private static Logger LOG = LogManager.getLogger(Migrator.class.getName());

    private static final String DB_INFO_ROOT_KEY = CoreNamespace.scholagestNs + "#dbInfo";
    private static final String DB_INFO_HASH_KEY = "pHash";

    public static void main(String[] args) throws JAXBException, IOException {
        new Migrator().initialize("change_set.xml").run();
    }

    private List<TChangeSet> changeSetList = new ArrayList<>();

    private Migrator initialize(String globalChangeSetFilename) throws JAXBException, IOException {
        changeSetList = new ChangeSetReader().getChangeSetListForGlobalChangeSetFile(globalChangeSetFilename);

        return this;
    }

    private void run() {
        IDatabase database = new Database(new DefaultDatabaseConfiguration());
        ITransaction transaction = database.getTransaction(ConfigurationService.getInstance().getStringProperty(ScholagestProperty.KEYSPACE));
        ScholagestThreadLocal.setTransaction(transaction);
        ScholagestThreadLocal.setRequestId(UUID.randomUUID().toString());

        try {
            updateDbWithChangeSet();

            transaction.commit();
        } catch (Exception e) {
            LOG.error("Error while migrating the DB", e);
            transaction.rollback();
        }
    }

    private void updateDbWithChangeSet() {
        List<TChangeSet> subsetToExecute = findChangeSetToExecute(changeSetList);

        for (TChangeSet changeSet : subsetToExecute) {
            LOG.info("Apply change set with id=" + changeSet.getId());
            applyChangeSetToAllKeysInTransaction(changeSet);

            ScholagestThreadLocal.getTransaction().insert(DB_INFO_ROOT_KEY, DB_INFO_HASH_KEY, changeSet.getId(), null);
        }
    }

    private List<TChangeSet> findChangeSetToExecute(List<TChangeSet> changeSetToFilter) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String currentHash = (String) transaction.get(DB_INFO_ROOT_KEY, DB_INFO_HASH_KEY, null);

        if (currentHash == null || currentHash.isEmpty()) {
            return changeSetToFilter;
        }

        for (int i = 0; i < changeSetToFilter.size(); i++) {
            if (currentHash.equals(changeSetToFilter.get(i).getId())) {
                logIgnoredChangeSets(changeSetToFilter.subList(0, i + 1));
                return changeSetToFilter.subList(i + 1, changeSetToFilter.size());
            }
        }

        throw new RuntimeException(String.format("Cannot determine the current state of the DB because no file matches the hash. hash=%s",
                currentHash));
    }

    private void logIgnoredChangeSets(List<TChangeSet> ignoredChangeSetList) {
        for (TChangeSet changeSet : ignoredChangeSetList) {
            LOG.info(String.format("Change set with id=%s already applied on the db", changeSet.getId()));
        }
    }

    private void applyChangeSetToAllKeysInTransaction(TChangeSet changeSet) {
        List<TDbUpdate> allUpdates = changeSet.getUpdates().getUpdates();
        List<RowsUpdater> rowsUpdaterList = new UpdaterFactory().getRowsUpdaterList(allUpdates);

        for (RowsUpdater rowsUpdater : rowsUpdaterList) {
            rowsUpdater.updateAllRows();
        }
    }
}
