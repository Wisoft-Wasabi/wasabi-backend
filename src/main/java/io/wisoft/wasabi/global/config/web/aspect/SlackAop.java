package io.wisoft.wasabi.global.config.web.aspect;

import io.wisoft.wasabi.global.config.web.response.Response;
import io.wisoft.wasabi.global.config.web.response.ResponseType;
import io.wisoft.wasabi.global.config.web.slack.SlackConstant;
import io.wisoft.wasabi.global.config.web.slack.SlackErrorMessage;
import io.wisoft.wasabi.global.config.web.slack.SlackService;
import io.wisoft.wasabi.global.exception.BusinessException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponse;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

@Aspect
@Component
public class SlackAop {
    private final SlackService slackService;
    private final ThreadPoolTaskExecutor executor;

    public SlackAop(final SlackService slackService, @Qualifier("asyncExecutor") final ThreadPoolTaskExecutor executor) {
        this.slackService = slackService;
        this.executor = executor;
    }

    @AfterThrowing(pointcut = "execution(* io.wisoft.wasabi.domain..*.*Service*.*(..)) || execution(* io.wisoft.wasabi.global..*(..))", throwing = "ex")
    public void afterThrowing(final BusinessException ex) {
        executor.execute(() -> {
            slackService.sendSlackMessage(new SlackErrorMessage(LocalDateTime.now(), ex.getErrorType()), SlackConstant.ERROR_CHANNEL);
        });
    }

}
