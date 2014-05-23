package net.scholagest.tester.utils.creator;

import net.scholagest.db.entity.ClazzEntity;
import net.scholagest.db.entity.PeriodEntity;

/**
 * Utility class to create {@link PeriodEntity}
 * 
 * @author CLA
 * @since 0.17.0
 */
public class PeriodEntityCreator {
    public static PeriodEntity createPeriodEntity(final String name, final ClazzEntity clazzEntity) {
        final PeriodEntity periodEntity = new PeriodEntity();
        periodEntity.setName(name);
        periodEntity.setClazz(clazzEntity);

        return periodEntity;
    }
}
