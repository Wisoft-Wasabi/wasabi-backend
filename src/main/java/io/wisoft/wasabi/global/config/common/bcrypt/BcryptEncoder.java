package io.wisoft.wasabi.global.config.common.bcrypt;

import org.mindrot.jbcrypt.BCrypt;

public class BcryptEncoder {

    private static String salt;

    public BcryptEncoder(final String salt) {
        BcryptEncoder.salt = salt;
    }

    public static String encrypt(final String password) {
        return BCrypt.hashpw(password, salt);
    }

    public static boolean isMatch(final String password, final String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
