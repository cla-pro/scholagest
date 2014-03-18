package net.scholagest.object;

import org.joda.time.DateTime;

/**
 * Session stored into the DB as transfer object. The session's key is the key given back to the page.
 * The session has an expiration date and a link to the user that owns this session.
 * 
 * @author CLA
 * @since 0.13.0
 */
public class Session extends Base {
    private DateTime expirationDate;
    private User user;

    public Session() {}

    public Session(final String id, final DateTime expirationDate, final User user) {
        super(id);
        this.expirationDate = expirationDate;
        this.user = user;
    }

    public DateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(final DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }
}
