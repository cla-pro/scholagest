package net.scholagest.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncoder {
    public String encodePassword(String password) {
        try {
            String passwordUtf8 = new String(password.getBytes(), "UTF-8");
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            byte[] encoded = digest.digest(passwordUtf8.getBytes());
            return new String(encoded, "UTF-8");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
