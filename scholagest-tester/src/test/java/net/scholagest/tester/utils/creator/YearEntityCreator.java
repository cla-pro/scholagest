package net.scholagest.tester.utils.creator;

import net.scholagest.db.entity.YearEntity;

/**
 * Utility class to create {@link YearEntity}
 * 
 * @author CLA
 * @since 0.17.0
 */
public class YearEntityCreator {
    public static YearEntity createYearEntity(final String name, final boolean running) {
        final YearEntity yearEntity = new YearEntity();
        yearEntity.setName(name);
        yearEntity.setRunning(running);

        return yearEntity;
    }
}
