package net.scholagest.old.shiro;

import java.util.UUID;

import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.old.database.DatabaseException;
import net.scholagest.old.managers.IUserManager;
import net.scholagest.old.managers.ontology.types.DBSet;
import net.scholagest.old.objects.TokenObject;
import net.scholagest.old.objects.UserObject;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.google.inject.Inject;

public class RealmAuthenticationAndAuthorization extends AuthorizingRealm {
    private static final String ROLES_KEY = "roles";
    private static final String PERMISSIONS_KEY = "permissions";
    public static final String TOKEN_KEY = "token";
    public static final int HASH_ITERATIONS = 1000;

    private final IUserManager userManager;

    @Inject
    public RealmAuthenticationAndAuthorization(IUserManager userManager) {
        this.userManager = userManager;

        setAuthenticationCachingEnabled(false);
        setAuthorizationCachingEnabled(false);

        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("SHA-1");
        credentialsMatcher.setStoredCredentialsHexEncoded(true);
        credentialsMatcher.setHashIterations(HASH_ITERATIONS);

        setCredentialsMatcher(credentialsMatcher);
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();

        for (Object roleObject : principals.fromRealm(ROLES_KEY)) {
            sai.addRole((String) roleObject);
        }

        for (Object permissionObject : principals.fromRealm(PERMISSIONS_KEY)) {
            sai.addStringPermission((String) permissionObject);
        }

        return sai;
    }

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ScholagestUsernameToken || token instanceof ScholagestTokenToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        if (token instanceof ScholagestUsernameToken) {
            return checkUsernameToken((ScholagestUsernameToken) token);
        } else if (token instanceof ScholagestTokenToken) {
            return checkToken((ScholagestTokenToken) token);
        }

        throw new AuthenticationException("Invalid informations");
    }

    private AuthenticationInfo checkUsernameToken(ScholagestUsernameToken token) {
        try {
            return getAndCheckUserFromDb(token);
        } catch (ScholagestRuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private AuthenticationInfo getAndCheckUserFromDb(ScholagestUsernameToken token) throws Exception {
        UserObject userObject = userManager.getUserWithUsername(token.getUsername());

        if (userObject != null) {
            if (isValidUsernamePassword(userObject, new String(token.getPassword()))) {
                TokenObject tokenObject = storeTokenForUser(UUID.randomUUID().toString(), userObject);
                return createAuthenticationInfo(userObject, tokenObject.getKey(), userObject.getPassword().toCharArray());
            } else {
                throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.WRONG_PASSWORD, "Wrong password for user: " + token.getUsername());
            }
        } else {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.USER_NOT_FOUND, "User with name " + token.getUsername() + " not found");
        }
    }

    private boolean isValidUsernamePassword(UserObject userObject, String password) {
        Sha1Hash hash = new Sha1Hash(password, userObject.getKey(), HASH_ITERATIONS);
        String encrypted = hash.toHex();

        return userObject != null && userObject.getPassword().equals(encrypted);
    }

    private TokenObject storeTokenForUser(String token, UserObject userObject) throws Exception {
        return userManager.createToken(userObject.getKey(), token);
    }

    private AuthenticationInfo checkToken(ScholagestTokenToken token) {
        try {
            return getAndCheckTokenFromDb(token);
        } catch (ScholagestRuntimeException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private AuthenticationInfo getAndCheckTokenFromDb(ScholagestTokenToken token) throws Exception {
        TokenObject tokenObject = userManager.getToken(token.getToken());

        if (isTokenValid(tokenObject)) {
            UserObject userObject = userManager.getUser(tokenObject.getUserObjectKey());
            return createAuthenticationInfo(userObject, token.getToken(), encryptToken(userObject.getKey(), token));
        } else {
            throw new ScholagestRuntimeException(ScholagestExceptionErrorCode.SESSION_EXPIRED, "Session expired");
        }
    }

    private String encryptToken(String userKey, ScholagestTokenToken token) {
        Sha1Hash hash = new Sha1Hash(token.getToken(), ByteSource.Util.bytes(userKey), HASH_ITERATIONS);
        return hash.toHex();
    }

    private boolean isTokenValid(TokenObject tokenObject) {
        return tokenObject != null && !isSessionExpired(tokenObject);
    }

    private boolean isSessionExpired(TokenObject tokenObject) {
        return tokenObject.getEndValidityTime().isBeforeNow();
    }

    private AuthenticationInfo createAuthenticationInfo(UserObject userObject, String token, Object credentials) throws Exception {
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userObject.getUsername(), credentials, getName());

        SimplePrincipalCollection principals = readRolesAndPermissions(userObject);
        principals.add(token, TOKEN_KEY);

        authenticationInfo.setPrincipals(principals);
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userObject.getKey()));

        return authenticationInfo;
    }

    private SimplePrincipalCollection readRolesAndPermissions(UserObject userObject) throws DatabaseException {
        SimplePrincipalCollection principals = new SimplePrincipalCollection();

        DBSet roles = userObject.getRoles();
        for (String role : roles.values()) {
            principals.add(role, ROLES_KEY);
        }

        DBSet permissions = userObject.getPermissions();
        for (String permission : permissions.values()) {
            principals.add(permission, PERMISSIONS_KEY);
        }

        return principals;
    }
}
