package io.wisoft.wasabi.global.config.web.slack;

public interface SlackService {

    void sendSlackMessage(final SlackErrorMessage message, final SlackConstant slackConstant);
    void sendSignUpMessage(final SlackSignUpMessage message, final SlackConstant slackConstant);
}
