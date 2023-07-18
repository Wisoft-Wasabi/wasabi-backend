package io.wisoft.wasabi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WasabiApplication {
    public static void main(String[] args) {
        SpringApplication.run(WasabiApplication.class, args);
    }
}
