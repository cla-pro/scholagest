package net.scholagest.authorization;

import net.scholagest.exception.ScholagestException;
import net.scholagest.exception.ScholagestExceptionErrorCode;
import net.scholagest.exception.ScholagestRuntimeException;
import net.scholagest.object.Session;
import net.scholagest.object.User;
import net.scholagest.old.database.DatabaseException;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
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

    @Inject
    public RealmAuthenticationAndAuthorization() {
        setAuthenticationCachingEnabled(false);
        setAuthorizationCachingEnabled(false);

        // final HashedCredentialsMatcher credentialsMatcher = new
        // HashedCredentialsMatcher("SHA-1");
        // credentialsMatcher.setStoredCredentialsHexEncoded(true);
        // credentialsMatcher.setHashIterations(HASH_ITERATIONS);

        setCredentialsMatcher(new AllowAllCredentialsMatcher());
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(final PrincipalCollection principals) {
        final SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();

        for (final Object roleObject : principals.fromRealm(ROLES_KEY)) {
            sai.addRole((String) roleObject);
        }

        for (final Object permissionObject : principals.fromRealm(PERMISSIONS_KEY)) {
            sai.addStringPermission((String) permissionObject);
        }

        return sai;
    }

    @Override
    public boolean supports(final AuthenticationToken token) {
        return token instanceof UsernameToken || token instanceof SessionToken;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(final AuthenticationToken token) throws AuthenticationException {
        try {
            return getToken(token);
        } catch (final ScholagestException e) {
            throw new AuthenticationException(e);
        } catch (final ScholagestRuntimeException e) {
            throw new AuthenticationException(e);
        } catch (final Exception e) {
            throw new AuthenticationException("Error during the authentication process", e);
        }
    }

    private AuthenticationInfo getToken(final AuthenticationToken token) throws ScholagestException {
        if (token instanceof UsernameToken) {
            return checkUsernameToken((UsernameToken) token);
        } else if (token instanceof SessionToken) {
            return checkSessionToken((SessionToken) token);
        } else {
            throw new ScholagestException(ScholagestExceptionErrorCode.GENERAL, "Unknown token type: " + token.getClass().getName());
        }
    }

    private AuthenticationInfo checkUsernameToken(final UsernameToken token) throws ScholagestException {
        final User user = null;

        if (user == null) {
            throw new ScholagestException(ScholagestExceptionErrorCode.USER_NOT_FOUND, "User with name " + token.getUsername() + " not found");
        } else {
            return createAuthenticationInfo(user, user.getPassword().toCharArray());
        }
    }

    // private boolean isValidUsernamePassword(final UserObject userObject,
    // final String password) {
    // final Sha1Hash hash = new Sha1Hash(password, userObject.getKey(),
    // HASH_ITERATIONS);
    // final String encrypted = hash.toHex();
    //
    // return userObject != null && userObject.getPassword().equals(encrypted);
    // }

    private AuthenticationInfo checkSessionToken(final SessionToken token) throws ScholagestException {
        final Session session = null; // userManager.getToken(token.getToken());

        if (isSessionValid(session)) {
            final User user = session.getUser();
            return createAuthenticationInfo(user, encryptToken(user.getId(), token.getToken()));
        } else {
            throw new ScholagestException(ScholagestExceptionErrorCode.SESSION_EXPIRED, "Session expired");
        }
    }

    private String encryptToken(final String userKey, final String token) {
        final Sha1Hash hash = new Sha1Hash(token, ByteSource.Util.bytes(userKey), HASH_ITERATIONS);
        return hash.toHex();
    }

    private boolean isSessionValid(final Session session) {
        return session != null && !isSessionExpired(session);
    }

    private boolean isSessionExpired(final Session session) {
        return session.getExpirationDate().isBeforeNow();
    }

    private AuthenticationInfo createAuthenticationInfo(final User user, final Object credentials) {
        final SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), credentials, getName());

        final SimplePrincipalCollection principals = readRolesAndPermissions(user);

        authenticationInfo.setPrincipals(principals);
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(user.getId()));

        return authenticationInfo;
    }

    private SimplePrincipalCollection readRolesAndPermissions(final User user) throws DatabaseException {
        final SimplePrincipalCollection principals = new SimplePrincipalCollection();

        principals.add(user.getRole(), ROLES_KEY);

        for (final String permission : user.getPermissions()) {
            principals.add(permission, PERMISSIONS_KEY);
        }

        return principals;
    }
}
