package net.scholagest.app.rest.ws.objects;

public class Exam extends BaseJson {
    private String name;
    private int coeff;
    private String branchPeriod;

    public Exam() {}

    public Exam(final String id, final String name, final int coeff, final String branchPeriod) {
        super(id);
        this.name = name;
        this.coeff = coeff;
        this.branchPeriod = branchPeriod;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getCoeff() {
        return coeff;
    }

    public void setCoeff(final int coeff) {
        this.coeff = coeff;
    }

    public String getBranchPeriod() {
        return branchPeriod;
    }
}
