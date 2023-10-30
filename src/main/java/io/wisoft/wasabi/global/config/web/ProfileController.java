package io.wisoft.wasabi.global.config.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ProfileController {
    private final Environment env;

    @GetMapping("/profile")
    public String profile() {

        log.error(Arrays.toString(env.getActiveProfiles()));

        return Arrays.stream(env.getActiveProfiles())
                .findFirst()
                .orElse("");
    }
}
