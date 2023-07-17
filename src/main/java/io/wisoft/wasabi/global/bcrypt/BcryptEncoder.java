package io.wisoft.wasabi.global.bcrypt;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptEncoder implements EncryptHelper {
    @Override
    public String encrypt(final String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    @Override
    public boolean isMatch(final String password, final String hashed) {
        return BCrypt.checkpw(password, hashed);
    }
}
