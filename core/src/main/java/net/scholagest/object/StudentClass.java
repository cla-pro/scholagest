package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object associate to {@link Student} that contains the classes information of a student.
 * 
 * @author CLA
 * @since 0.15.0
 */
public class StudentClass extends Base {
    private final List<String> currentClasses;
    private final List<String> oldClasses;

    public StudentClass() {
        this.currentClasses = new ArrayList<>();
        this.oldClasses = new ArrayList<>();
    }

    public StudentClass(final StudentClass toCopy) {
        this(toCopy.getId(), toCopy.currentClasses, toCopy.oldClasses);
    }

    public StudentClass(final String id, final List<String> currentClasses, final List<String> oldClasses) {
        super(id);
        this.currentClasses = new ArrayList<>(currentClasses);
        this.oldClasses = new ArrayList<>(oldClasses);
    }

    public List<String> getCurrentClasses() {
        return currentClasses;
    }

    public List<String> getOldClasses() {
        return oldClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof StudentClass)) {
            return false;
        }

        final StudentClass other = (StudentClass) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(currentClasses, other.currentClasses).append(oldClasses, other.oldClasses)
                .isEquals();
    }
}
