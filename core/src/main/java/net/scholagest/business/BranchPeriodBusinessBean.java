package net.scholagest.business;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.scholagest.object.BranchPeriod;

/**
 * Implementation of {@link BranchBusinessLocal}
 * 
 * @author CLA
 * @since 0.14.0
 */
public class BranchPeriodBusinessBean implements BranchPeriodBusinessLocal {
    public static Map<String, BranchPeriod> branchPeriodsMap = new HashMap<>();

    static {
        branchPeriodsMap.put("branchPeriod1",
                new BranchPeriod("branchPeriod1", "branch1", "period1", Arrays.asList("exam1", "exam2"), Arrays.asList("studentResult1")));
        branchPeriodsMap.put("branchPeriod2", new BranchPeriod("branchPeriod2", "branch2", "period1", Arrays.asList("exam3", "exam4", "exam5"),
                Arrays.asList("studentResult2")));
        branchPeriodsMap.put("branchPeriod3", new BranchPeriod("branchPeriod3", "branch1", "period2", new ArrayList<String>(),
                new ArrayList<String>()));
        branchPeriodsMap.put("branchPeriod4", new BranchPeriod("branchPeriod4", "branch2", "period2", new ArrayList<String>(),
                new ArrayList<String>()));
        branchPeriodsMap.put("branchPeriod5", new BranchPeriod("branchPeriod5", "branch1", "period3", new ArrayList<String>(),
                new ArrayList<String>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BranchPeriod getBranchPeriod(final String id) {
        if (branchPeriodsMap.containsKey(id)) {
            return new BranchPeriod(branchPeriodsMap.get(id));
        } else {
            return null;
        }
    }
}
