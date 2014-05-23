package net.scholagest.tester.utils.creator;

import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.YearEntity;

/**
 * Utility class to create {@link ClassEntity}
 * 
 * @author CLA
 * @since 0.17.0
 */
public class ClazzEntityCreator {
    public static ClazzEntity createClazzEntity(final String name, final YearEntity yearEntity) {
        final ClazzEntity classEntity = new ClazzEntity();
        classEntity.setName(name);
        classEntity.setYear(yearEntity);

        return classEntity;
    }
}
