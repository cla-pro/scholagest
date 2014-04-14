package net.scholagest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a result in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "result")
public class ResultEntity {
    private final static String TOSTRING_FORMAT = "Result [id=%d, grade=%s]";

    @Id
    @SequenceGenerator(name = "seq_result", sequenceName = "seq_result_id")
    @GeneratedValue(generator = "seq_result", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "active")
    private String grade;

    @JoinColumn(name = "exam_id")
    @ManyToOne
    private ExamEntity exam;

    @JoinColumn(name = "student_result_id")
    @ManyToOne
    private StudentResultEntity studentResult;

    public ResultEntity() {}

    public Long getId() {
        return id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(final String grade) {
        this.grade = grade;
    }

    public ExamEntity getExam() {
        return exam;
    }

    public void setExam(final ExamEntity exam) {
        this.exam = exam;
    }

    public StudentResultEntity getStudentResult() {
        return studentResult;
    }

    public void setStudentResult(final StudentResultEntity studentResult) {
        this.studentResult = studentResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, grade);
    }
}
