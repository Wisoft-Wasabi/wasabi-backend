package io.wisoft.wasabi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class WasabiApplication {

//    public static final String APPLICATION_LOCATIONS =
//            "spring.config.location=classpath:application.yml," +
//            "classpath:application-real.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(WasabiApplication.class)
//                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
}
