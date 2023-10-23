package io.wisoft.wasabi.global.config.web;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {

        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
