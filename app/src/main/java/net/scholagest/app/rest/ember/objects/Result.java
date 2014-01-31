package net.scholagest.app.rest.ember.objects;

public class Result {
    private String id;
    private double grade;
    private String student;
    private final String exam;

    public Result(final String id, final double grade, final String exam, final String student) {
        this.id = id;
        this.grade = grade;
        this.exam = exam;
        this.student = student;
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

    public String getStudent() {
        return student;
    }

    public void setStudent(String student) {
        this.student = student;
    }
}
