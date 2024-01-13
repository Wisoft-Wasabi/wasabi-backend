package io.wisoft.wasabi.global.config.common.bcrypt;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BcryptEncoder {

    private static String salt;

    public BcryptEncoder(@Value("${bcrypt.secret.salt}") final String salt) {
        BcryptEncoder.salt = salt;
    }

    public static String encrypt(final String password) {
        return BCrypt.hashpw(password, salt);
    }

    public static boolean isMatch(final String password, final String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
