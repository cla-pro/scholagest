package net.scholagest.managers.impl;

import java.util.UUID;

import net.scholagest.database.ITransaction;
import net.scholagest.managers.IOntologyManager;
import net.scholagest.managers.IUserManager;
import net.scholagest.managers.ontology.types.DBSet;
import net.scholagest.namespace.CoreNamespace;
import net.scholagest.objects.ObjectHelper;
import net.scholagest.objects.TokenObject;
import net.scholagest.objects.UserObject;
import net.scholagest.utils.ScholagestThreadLocal;

import org.joda.time.DateTime;

import com.google.inject.Inject;

public class UserManager extends ObjectManager implements IUserManager {
    @Inject
    public UserManager(IOntologyManager ontologyManager) {
        super(ontologyManager);
    }

    @Override
    public UserObject createUser(String username, String password) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        UserObject userObject = createUserObject(transaction, username, password);

        persistObject(transaction, userObject);
        transaction.insert(CoreNamespace.userBase, username, userObject.getKey(), null);

        return userObject;
    }

    private UserObject createUserObject(ITransaction transaction, String username, String password) {
        UserObject userObject = new UserObject(UUID.randomUUID().toString());

        userObject.setUsername(username);
        userObject.setPassword(password);
        userObject.setPermissions(new DBSet(transaction, UUID.randomUUID().toString()));
        userObject.setRoles(new DBSet(transaction, UUID.randomUUID().toString()));

        return userObject;
    }

    @Override
    public UserObject getUser(String userKey) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new UserObject(transaction, new ObjectHelper(getOntologyManager()), userKey);
    }

    @Override
    public UserObject getUserWithUsername(String username) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String userKey = (String) transaction.get(CoreNamespace.userBase, username, null);
        if (userKey != null) {
            return getUser(userKey);
        }
        return null;
    }

    @Override
    public TokenObject createToken(String userKey, String tokenId) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        TokenObject tokenObject = createTokenObject(userKey, tokenId);

        persistObject(transaction, tokenObject);

        transaction.insert(CoreNamespace.tokenBase, tokenObject.getKey(), userKey, null);

        return tokenObject;
    }

    private TokenObject createTokenObject(String userKey, String tokenId) {
        TokenObject tokenObject = new TokenObject(tokenId);

        tokenObject.setUserObjectKey(userKey);
        tokenObject.setEndValidityTime(new DateTime().plusHours(2));

        return tokenObject;
    }

    @Override
    public TokenObject getToken(String tokenId) {
        TokenObject tokenObject = null;

        if (isTokenExists(tokenId)) {
            return readTokenObject(tokenId);
        }

        return tokenObject;
    }

    private TokenObject readTokenObject(String tokenId) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return new TokenObject(transaction, new ObjectHelper(getOntologyManager()), tokenId);
    }

    private boolean isTokenExists(String tokenId) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        return transaction.get(CoreNamespace.tokenBase, tokenId, null) != null;
    }

    @Override
    public void deleteToken(String tokenId) {
        ITransaction transaction = ScholagestThreadLocal.getTransaction();

        String tokenKey = (String) transaction.get(CoreNamespace.tokenBase, tokenId, null);
        if (tokenKey != null) {
            transaction.delete(CoreNamespace.tokenBase, tokenId, null);
            deleteObject(transaction, tokenKey);
        }
    }
}
