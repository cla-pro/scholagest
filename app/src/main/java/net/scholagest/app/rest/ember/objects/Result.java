package net.scholagest.app.rest.ember.objects;

public class Result {
    private String id;
    private double grade;
    private String studentResult;
    private String exam;

    public Result() {}

    public Result(final String id, final double grade, final String exam, final String studentResult) {
        this.id = id;
        this.grade = grade;
        this.exam = exam;
        this.studentResult = studentResult;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public String getExam() {
        return exam;
    }

    public String getStudentResult() {
        return studentResult;
    }

    public void setStudentResult(String studentResult) {
        this.studentResult = studentResult;
    }
}
