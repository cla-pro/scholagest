package net.scholagest.app.rest.ember.objects;

public class Exam extends Base {
    private String name;
    private int coeff;
    private String branch;

    public Exam() {}

    public Exam(final String id, final String name, final int coeff, final String branch) {
        super(id);
        this.name = name;
        this.coeff = coeff;
        this.branch = branch;
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

    public String getBranch() {
        return branch;
    }
}
