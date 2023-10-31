package io.wisoft.wasabi.global.config.common.bcrypt;

public interface EncryptHelper {
    String encrypt(String password);
    boolean isMatch(String password, String hashed);
}
