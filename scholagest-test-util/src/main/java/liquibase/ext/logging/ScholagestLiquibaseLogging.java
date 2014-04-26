package liquibase.ext.logging;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.logging.core.AbstractLogger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScholagestLiquibaseLogging extends AbstractLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScholagestLiquibaseLogging.class);
    private String name = "";

    @Override
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public void severe(final String message) {
        LOGGER.error("{} {}", name, message);
    }

    @Override
    public void severe(final String message, final Throwable e) {
        LOGGER.error("{} {}", name, message, e);
    }

    @Override
    public void warning(final String message) {
        LOGGER.warn("{} {}", name, message);
    }

    @Override
    public void warning(final String message, final Throwable e) {
        LOGGER.warn("{} {}", name, message, e);
    }

    @Override
    public void info(final String message) {
        LOGGER.info("{} {}", name, message);
    }

    @Override
    public void info(final String message, final Throwable e) {
        LOGGER.info("{} {}", name, message, e);
    }

    @Override
    public void debug(final String message) {
        LOGGER.debug("{} {}", name, message);
    }

    @Override
    public void debug(final String message, final Throwable e) {
        LOGGER.debug("{} {}", message, e);
    }

    @Override
    public void setLogLevel(final String logLevel, final String logFile) {}

    @Override
    public void setChangeLog(final DatabaseChangeLog databaseChangeLog) {}

    @Override
    public void setChangeSet(final ChangeSet changeSet) {}

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }
}
