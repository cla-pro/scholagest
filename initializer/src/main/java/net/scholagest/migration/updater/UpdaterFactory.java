package net.scholagest.migration.updater;

import java.util.ArrayList;
import java.util.List;

import net.scholagest.initializer.jaxb.TChangeProperty;
import net.scholagest.initializer.jaxb.TCreateNode;
import net.scholagest.initializer.jaxb.TCreateProperty;
import net.scholagest.initializer.jaxb.TCreateType;
import net.scholagest.initializer.jaxb.TDbUpdate;
import net.scholagest.initializer.jaxb.TDeleteNode;
import net.scholagest.initializer.jaxb.TRemoveProperty;
import net.scholagest.initializer.jaxb.TRemoveType;
import net.scholagest.initializer.jaxb.TRenameProperty;
import net.scholagest.initializer.jaxb.TRenameType;
import net.scholagest.initializer.jaxb.TSetProperty;
import net.scholagest.migration.RowsUpdater;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpdaterFactory {
    private static final Logger LOG = LogManager.getLogger(UpdaterFactory.class);

    public List<RowsUpdater> getRowsUpdaterList(List<TDbUpdate> dbUpdateList) {
        List<RowsUpdater> rowsUpdaterList = new ArrayList<>();

        for (TDbUpdate dbUpdate : dbUpdateList) {
            rowsUpdaterList.add(getRowsUpdater(dbUpdate));
        }

        return rowsUpdaterList;
    }

    private RowsUpdater getRowsUpdater(TDbUpdate dbUpdate) {
        if (dbUpdate instanceof TChangeProperty) {
            LOG.warn("Update with type TChangeProperty ignored");
        } else if (dbUpdate instanceof TCreateNode) {
            return new CreateNodeRowsUpdater((TCreateNode) dbUpdate);
        } else if (dbUpdate instanceof TCreateProperty) {
            return new CreatePropertyRowsUpdater((TCreateProperty) dbUpdate);
        } else if (dbUpdate instanceof TCreateType) {
            return new CreateTypeRowsUpdater((TCreateType) dbUpdate);
        } else if (dbUpdate instanceof TDeleteNode) {
            return new DeleteNodeRowsUpdater((TDeleteNode) dbUpdate);
        } else if (dbUpdate instanceof TRemoveProperty) {
            return new RemovePropertyRowsUpdater((TRemoveProperty) dbUpdate);
        } else if (dbUpdate instanceof TRemoveType) {
            LOG.warn("Update with type TRemoveType ignored");
        } else if (dbUpdate instanceof TRenameProperty) {
            return new RenamePropertyRowsUpdater((TRenameProperty) dbUpdate);
        } else if (dbUpdate instanceof TRenameType) {
            return new RenameTypeRowsUpdater((TRenameType) dbUpdate);
        } else if (dbUpdate instanceof TSetProperty) {
            return new SetPropertyRowsUpdater((TSetProperty) dbUpdate);
        }

        return null;
    }
}
