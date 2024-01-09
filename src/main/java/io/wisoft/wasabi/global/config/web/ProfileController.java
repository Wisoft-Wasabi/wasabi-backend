package io.wisoft.wasabi.global.config.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final Environment env;
    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @ResponseBody
    @GetMapping("/profile")
    public String profile() {

        logger.error(System.getProperty("spring.profiles.active"));

        return System.getProperty("spring.profiles.active");
    }
}
