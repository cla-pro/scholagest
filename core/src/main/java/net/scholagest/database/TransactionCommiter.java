package net.scholagest.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.prettyprint.hector.api.exceptions.HectorException;
import net.scholagest.database.commiter.DBAction;

class TransactionCommiter {
    private List<DBAction> dbActions = new ArrayList<>();

    void addAction(DBAction dbAction) {
        dbActions.add(dbAction);
    }

    void commit() {
        List<DBAction> commitedActions = new ArrayList<>();
        DBAction currentAction = null;
        try {
            for (DBAction dbAction : dbActions) {
                currentAction = dbAction;
                dbAction.commit();
                commitedActions.add(dbAction);
            }
        } catch (HectorException t) {
            rollback(commitedActions, currentAction);
            throw t;
        }
    }

    void rollback() {
        // Do nothing because nothing has been executed onto the DB.
    }

    private void rollback(List<DBAction> commitedActions, DBAction exceptionTriggerAction) {
        ArrayList<DBAction> clonedCommitedActions = new ArrayList<>(commitedActions);
        Collections.reverse(clonedCommitedActions);

        if (exceptionTriggerAction != null) {
            rollbackSingleAction(exceptionTriggerAction);
        }

        for (DBAction dbAction : clonedCommitedActions) {
            rollbackSingleAction(dbAction);
        }
    }

    private HectorException rollbackSingleAction(DBAction exceptionTriggerAction) {
        try {
            exceptionTriggerAction.rollback();
        } catch (HectorException e) {
            return e;
        }
        return null;
    }
}
