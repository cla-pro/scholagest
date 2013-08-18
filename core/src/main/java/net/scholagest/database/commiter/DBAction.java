package net.scholagest.database.commiter;

import me.prettyprint.hector.api.exceptions.HectorException;

public interface DBAction {
    public void commit() throws HectorException;

    public void rollback() throws HectorException;

    public String getKey();

    public String getColumn();

    public String getValue();
}
