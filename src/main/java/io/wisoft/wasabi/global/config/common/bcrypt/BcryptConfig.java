package io.wisoft.wasabi.global.config.common.bcrypt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BcryptConfig {

    @Value("${bcrypt.secret.salt}")
    private String salt;

    @Bean
    public BcryptEncoder BcryptEncoder() {
        return new BcryptEncoder(salt);
    }
}
