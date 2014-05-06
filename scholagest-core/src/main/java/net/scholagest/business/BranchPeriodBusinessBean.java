package net.scholagest.business;

import net.scholagest.converter.BranchPeriodEntityConverter;
import net.scholagest.dao.BranchPeriodDaoLocal;
import net.scholagest.db.entity.BranchPeriodEntity;
import net.scholagest.object.BranchPeriod;

import com.google.inject.Inject;

/**
 * Implementation of {@link BranchPeriodBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriodBusinessBean implements BranchPeriodBusinessLocal {
    // public static Map<String, BranchPeriod> branchPeriodsMap = new
    // HashMap<>();
    //
    // static {
    // branchPeriodsMap.put("branchPeriod1", new BranchPeriod("branchPeriod1",
    // "branch1", "period1", Arrays.asList("exam1", "exam2"), "exam6",
    // Arrays.asList("studentResult1")));
    // branchPeriodsMap.put("branchPeriod2", new BranchPeriod("branchPeriod2",
    // "branch2", "period1", new ArrayList<String>(), "exam8",
    // new ArrayList<String>()));
    // branchPeriodsMap.put("branchPeriod3", new BranchPeriod("branchPeriod3",
    // "branch1", "period2", new ArrayList<String>(), "exam9",
    // new ArrayList<String>()));
    // branchPeriodsMap.put("branchPeriod4", new BranchPeriod("branchPeriod4",
    // "branch2", "period2", Arrays.asList("exam3", "exam4", "exam5"),
    // "exam7", Arrays.asList("studentResult2")));
    // branchPeriodsMap.put("branchPeriod5", new BranchPeriod("branchPeriod5",
    // "branch1", "period3", new ArrayList<String>(), "exam10",
    // new ArrayList<String>()));
    // }

    @Inject
    private BranchPeriodDaoLocal branchPeriodDao;

    BranchPeriodBusinessBean() {}

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchPeriod getBranchPeriod(final Long id) {
        final BranchPeriodEntity branchPeriodEntity = branchPeriodDao.getBranchPeriodEntityById(id);

        if (branchPeriodEntity == null) {
            return null;
        } else {
            final BranchPeriodEntityConverter branchPeriodEntityConverter = new BranchPeriodEntityConverter();
            return branchPeriodEntityConverter.convertToBranchPeriod(branchPeriodEntity);
        }
    }
}
