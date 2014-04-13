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
 * Entity that model a class in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "class")
public class ClazzEntity {
    private final static String TOSTRING_FORMAT = "Class [id=%d, name=%s]";

    @Id
    @SequenceGenerator(name = "seq_class", sequenceName = "seq_class_id")
    @GeneratedValue(generator = "seq_class", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "year_id")
    @ManyToOne
    private YearEntity year;

    public ClazzEntity() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public YearEntity getYear() {
        return year;
    }

    public void setYear(final YearEntity year) {
        this.year = year;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, name);
    }
}
