package net.scholagest.db.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.joda.time.DateTime;

/**
 * Entity that model a session in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "session")
public class SessionEntity {
    private final static String TOSTRING_FORMAT = "Session [id=%d, expirationDate=%s, userId=%s]";

    @Id
    private String id;

    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private UserEntity user;

    public SessionEntity() {}

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public DateTime getExpirationDate() {
        if (expirationDate == null) {
            return null;
        } else {
            return new DateTime(expirationDate.getTime());
        }
    }

    public void setExpirationDate(final DateTime expirationDate) {
        this.expirationDate = new Timestamp(expirationDate.getMillis());
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(final UserEntity user) {
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, expirationDate, user.getId());
    }
}
