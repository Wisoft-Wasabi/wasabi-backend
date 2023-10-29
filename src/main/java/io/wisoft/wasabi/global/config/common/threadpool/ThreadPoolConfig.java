package io.wisoft.wasabi.global.config.common.threadpool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig {

    @Bean(name = "asyncExecutor")
    public ThreadPoolTaskExecutor asyncExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.initialize();

        return executor;
    }
}
