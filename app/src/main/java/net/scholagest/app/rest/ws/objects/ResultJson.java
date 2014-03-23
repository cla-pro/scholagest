package net.scholagest.app.rest.ws.objects;

public class ResultJson extends BaseJson {
    private Double grade;
    private String studentResult;
    private String exam;
    private int resultCounter;

    public ResultJson() {}

    public ResultJson(final String id, final Double grade, final String exam, final String studentResult) {
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

    public void setExam(final String exam) {
        this.exam = exam;
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
