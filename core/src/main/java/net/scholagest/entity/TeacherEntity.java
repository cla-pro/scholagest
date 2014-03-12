package net.scholagest.entity;

public class TeacherEntity extends BaseEntity {
    private String firstName;
    private String lastName;
    private TeacherDetailEntity teacherDetail;

    public TeacherEntity() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public TeacherDetailEntity getTeacherDetail() {
        return teacherDetail;
    }

    public void setTeacherDetail(final TeacherDetailEntity teacherDetail) {
        this.teacherDetail = teacherDetail;
    }
}
