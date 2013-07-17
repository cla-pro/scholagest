package net.scholagest.objects;

import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;

public class BranchObject extends BaseObject {
    public BranchObject(String key) {
        super(key, CoreNamespace.tBranch);
    }

    public String getClassKey() {
        return (String) getProperty(CoreNamespace.pBranchClass);
    }

    public void setClassKey(String classKey) {
        putProperty(CoreNamespace.pBranchClass, classKey);
    }

    public BranchType getBranchType() {
        String branchType = (String) getProperty(CoreNamespace.pBranchType);
        if (branchType == null) {
            setBranchType(BranchType.NUMERICAL);
            return BranchType.NUMERICAL;
        }

        return BranchType.valueOf(branchType);
    }

    public void setBranchType(BranchType branchType) {
        putProperty(CoreNamespace.pBranchType, branchType.name());
    }

    public DBSet getPeriods() {
        return (DBSet) getProperty(CoreNamespace.pBranchPeriods);
    }

    public void setPeriods(DBSet periods) {
        putProperty(CoreNamespace.pBranchPeriods, periods);
    }

    public String getMeanKey() {
        return (String) getProperty(CoreNamespace.pBranchMean);
    }

    public void setMeanKey(String meanKey) {
        putProperty(CoreNamespace.pBranchMean, meanKey);
    }
}
