package net.scholagest.db.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

/**
 * Entity that model a year in the DB
 * 
 * @author CLA
 * @since 0.16.0
 */
@Entity(name = "year")
public class YearEntity {
    private final static String TOSTRING_FORMAT = "Year [id=%d, name=%s]";

    @Id
    @SequenceGenerator(name = "seq_year", sequenceName = "seq_year_id")
    @GeneratedValue(generator = "seq_year", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "running")
    private boolean running;

    @OneToMany(mappedBy = "year")
    private List<ClazzEntity> classes;

    public YearEntity() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(final boolean running) {
        this.running = running;
    }

    public List<ClazzEntity> getClasses() {
        if (classes == null) {
            return new ArrayList<>();
        } else {
            return classes;
        }
    }

    public void setClasses(final List<ClazzEntity> classes) {
        this.classes = classes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(TOSTRING_FORMAT, id, name);
    }
}
