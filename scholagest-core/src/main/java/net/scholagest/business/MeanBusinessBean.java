package net.scholagest.business;

import net.scholagest.converter.MeanEntityConverter;
import net.scholagest.dao.MeanDaoLocal;
import net.scholagest.db.entity.MeanEntity;
import net.scholagest.object.Mean;

import com.google.inject.Inject;

/**
 * Implementation of {@link ClazzBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class MeanBusinessBean implements MeanBusinessLocal {

    @Inject
    private MeanDaoLocal meanDao;

    MeanBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public Mean getMean(final Long id) {
        final MeanEntity meanEntity = meanDao.getMeanEntityById(id);

        if (meanEntity == null) {
            return null;
        } else {
            final MeanEntityConverter meanEntityConverter = new MeanEntityConverter();
            return meanEntityConverter.convertToMean(meanEntity);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Mean saveMean(final Mean mean) {
        final MeanEntity meanEntity = meanDao.getMeanEntityById(Long.valueOf(mean.getId()));

        if (meanEntity == null) {
            return null;
        } else {
            meanEntity.setGrade(mean.getGrade());

            final MeanEntityConverter meanEntityConverter = new MeanEntityConverter();
            return meanEntityConverter.convertToMean(meanEntity);
        }
    }
}
