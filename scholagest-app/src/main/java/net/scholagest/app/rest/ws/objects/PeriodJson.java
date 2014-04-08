package net.scholagest.app.rest.ws.objects;

import java.util.ArrayList;
import java.util.List;

/**
 * Json object representing a period
 * 
 * @author CLA
 * @since 0.14.0
 */
public class PeriodJson extends BaseJson {
    private String name;
    private String clazz;
    private List<String> branchPeriods;

    public PeriodJson() {}

    public PeriodJson(final String id, final String name, final String clazz, final List<String> branchPeriods) {
        super(id);
        this.name = name;
        this.clazz = clazz;
        this.branchPeriods = new ArrayList<String>(branchPeriods);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }

    public List<String> getBranchPeriods() {
        return branchPeriods;
    }

    public void setBranchPeriods(final List<String> branchPeriods) {
        this.branchPeriods = branchPeriods;
    }
}
