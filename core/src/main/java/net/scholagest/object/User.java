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
    private String teacherId;
    private String role;
    private List<String> permissions;

    public User() {}

    public User(final User toCopy) {
        this.username = toCopy.username;
        this.password = toCopy.password;
        this.teacherId = toCopy.teacherId;
        this.role = toCopy.role;
        this.permissions = new ArrayList<>(toCopy.permissions);
    }

    public User(final String id, final String username, final String password, final String role, final List<String> permissions,
            final String teacherId) {
        super(id);
        this.username = username;
        this.password = password;
        this.teacherId = teacherId;
        this.role = role;
        this.permissions = permissions;
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

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(final String teacherId) {
        this.teacherId = teacherId;
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
        return new EqualsBuilder().append(getId(), other.getId()).append(teacherId, other.teacherId).isEquals();
    }
}
