package io.wisoft.wasabi.domain.auth.application;

import io.wisoft.wasabi.global.config.common.Const;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class EmailServiceImpl implements EmailService {

    private final String from;
    private final ThreadPoolTaskExecutor executor;
    private final JavaMailSender emailSender;

    public EmailServiceImpl(
        @Value("${spring.mail.username}") final String from,
        @Qualifier("asyncExecutor") final ThreadPoolTaskExecutor executor,
        final JavaMailSender emailSender) {
        this.from = from;
        this.emailSender = emailSender;
        this.executor = executor;
    }

    @Override
    public String sendSimpleMessage(final String to) {

        final String randomNumber = randomNumber();
        final String text = String.format(Const.MAIL_AUTH_MESSAGE, randomNumber);

        final SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(Const.MAIL_SUBJECT);
        message.setText(text);

        executor.execute(() -> emailSender.send(message));

        return randomNumber;
    }

    private String randomNumber() {
        return padLeft(
            String.valueOf(
                ThreadLocalRandom
                    .current()
                    .nextInt(((int) Math.pow(10, 6)) - 1))
        );
    }

    private String padLeft(
        final String string
    ) {
        if (string.length() >= 6) {
            return string;
        } else {
            return "0".repeat(6 - string.length()) + string;
        }
    }
}
