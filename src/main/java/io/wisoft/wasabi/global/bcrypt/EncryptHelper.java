package io.wisoft.wasabi.global.bcrypt;

public interface EncryptHelper {
    String encrypt(String password, String salt);
    boolean isMatch(String password, String hashed);
}
