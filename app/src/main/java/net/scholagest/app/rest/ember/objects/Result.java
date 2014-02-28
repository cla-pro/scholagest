package net.scholagest.app.rest.ember.objects;

public class Result extends Base {
    private double grade;
    private String studentResult;
    private String exam;

    public Result() {}

    public Result(final String id, final double grade, final String exam, final String studentResult) {
        super(id);
        this.grade = grade;
        this.exam = exam;
        this.studentResult = studentResult;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(final double grade) {
        this.grade = grade;
    }

    public String getExam() {
        return exam;
    }

    public String getStudentResult() {
        return studentResult;
    }

    public void setStudentResult(final String studentResult) {
        this.studentResult = studentResult;
    }
}
