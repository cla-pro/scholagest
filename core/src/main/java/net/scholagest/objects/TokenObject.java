package net.scholagest.objects;

import net.scholagest.managers.impl.CoreNamespace;

import org.joda.time.DateTime;

public class TokenObject extends BaseObject {
    public TokenObject(String key) {
        super(key, CoreNamespace.tToken);
    }

    public DateTime getEndValidityTime() {
        return (DateTime) getProperty(CoreNamespace.pTokenEndValidityTime);
    }

    public void setEndValidityTime(DateTime endValidityTime) {
        putProperty(CoreNamespace.pTokenEndValidityTime, endValidityTime);
    }

    public String getUserObjectKey() {
        return (String) getProperty(CoreNamespace.pTokenUser);
    }

    public void setUserObjectKey(String userKey) {
        putProperty(CoreNamespace.pTokenUser, userKey);
    }
}
