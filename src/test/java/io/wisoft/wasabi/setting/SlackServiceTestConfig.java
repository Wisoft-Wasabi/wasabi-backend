package io.wisoft.wasabi.setting;

import io.wisoft.wasabi.global.config.web.slack.MockSlackService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class SlackServiceTestConfig {

    @Primary
    @Bean
    public MockSlackService mockSlackService(){
        return new MockSlackService();
    }

}
