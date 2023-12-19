package io.wisoft.wasabi.global.config.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequiredArgsConstructor
public class ProfileController {

    private final Environment env;

    @ResponseBody
    @GetMapping("/profile")
    public String profile() {

        log.error(System.getProperty("spring.profiles.active"));

        return System.getProperty("spring.profiles.active");
    }
}
