package net.scholagest.app.rest.ws.objects;

public class Result extends Base {
    private Double grade;
    private String studentResult;
    private String exam;
    private int resultCounter;

    public Result() {}

    public Result(final String id, final Double grade, final String exam, final String studentResult) {
        super(id);
        this.grade = grade;
        this.exam = exam;
        this.studentResult = studentResult;
        this.resultCounter = 0;
    }

    public Double getGrade() {
        return grade;
    }

    public void setGrade(final Double grade) {
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

    public int getResultCounter() {
        return resultCounter;
    }
}
