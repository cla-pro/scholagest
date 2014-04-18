package net.scholagest.object;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Transfer object for the user. It has a link to its underlying teacher.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class User extends Base {
    private String username;
    private String password;
    private String teacher;
    private String clazz;
    private String role;
    private List<String> permissions;

    public User() {
        this.permissions = new ArrayList<>();
    }

    public User(final User toCopy) {
        this(toCopy.getId(), toCopy.username, toCopy.password, toCopy.role, toCopy.permissions, toCopy.teacher, toCopy.clazz);
    }

    public User(final String id, final String username, final String password, final String role, final List<String> permissions,
            final String teacher, final String clazz) {
        super(id);
        this.username = username;
        this.password = password;
        this.teacher = teacher;
        this.role = role;
        this.permissions = permissions;
        this.clazz = clazz;
    }

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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(final String teacherId) {
        this.teacher = teacherId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(final String clazz) {
        this.clazz = clazz;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(final List<String> permissions) {
        this.permissions = permissions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object that) {
        if (that == null) {
            return false;
        } else if (!(that instanceof User)) {
            return false;
        }

        final User other = (User) that;
        return new EqualsBuilder().append(getId(), other.getId()).append(teacher, other.teacher).isEquals();
    }
}
