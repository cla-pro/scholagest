package net.scholagest.object;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object associate to {@link Student} that contains the medical information of a student.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class StudentMedical extends Base {
    private String doctor;

    public StudentMedical() {}

    public StudentMedical(final StudentMedical toCopy) {
        this(toCopy.getId(), toCopy.doctor);
    }

    public StudentMedical(final String id, final String doctor) {
        super(id);
        this.doctor = doctor;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(final String doctor) {
        this.doctor = doctor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof StudentMedical)) {
            return false;
        }

        final StudentMedical other = (StudentMedical) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(doctor, other.doctor).isEquals();
    }
}
