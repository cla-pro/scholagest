package net.scholagest.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a user in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "scholagest_user")
public class UserEntity {
    private final static String TOSTRING_FORMAT = "User [id=%d, username=%s, role=%s, teacher=%s]";

    @Id
    @SequenceGenerator(name = "seq_user", sequenceName = "seq_user_id")
    @GeneratedValue(generator = "seq_user", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    // TODO Map to an enum?
    @Column(name = "role")
    private String role;

    @JoinColumn(name = "teacher_id")
    @OneToOne
    private TeacherEntity teacher;

    public UserEntity() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public TeacherEntity getTeacher() {
        return teacher;
    }

    public void setTeacher(final TeacherEntity teacher) {
        this.teacher = teacher;
    }

    public Long getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, username, role, teacher);
    }
}
