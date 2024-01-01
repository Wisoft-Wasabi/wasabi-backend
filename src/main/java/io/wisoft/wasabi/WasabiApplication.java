package io.wisoft.wasabi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WasabiApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(WasabiApplication.class)
                .run(args);
    }
}
