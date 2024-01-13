package io.wisoft.wasabi.global.config.web.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Slf4j
@Service
@Transactional(readOnly = true)
public class SlackServiceImpl implements SlackService{

    @Value(value = "${slack.hooks.token}")
    private String slackToken;

    @Value(value = "${slack.hooks.sign-up-token}")
    private String signUpToken;

    @Async
    public void sendSlackMessage(final SlackErrorMessage message, final SlackConstant slackConstant) {

        final String channel = slackConstant.getChannel();

        try {
            final MethodsClient slackBot = Slack.getInstance().methods(slackToken);

            final ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message.toString())
                    .build();

            slackBot.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            log.error("", e);
        }
    }

    @Async
    public void sendSignUpMessage(final SlackSignUpMessage message, final SlackConstant slackConstant) {

        final String channel = slackConstant.getChannel();

        try {
            final MethodsClient slackBot = Slack.getInstance().methods(signUpToken);

            final ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message.toString())
                    .build();

            slackBot.chatPostMessage(request);
        } catch (SlackApiException | IOException e) {
            log.error("", e);
        }
    }
}
