package io.wisoft.wasabi.global.config.web.slack;

public class MockSlackService implements SlackService {
    public void sendSlackMessage(final SlackErrorMessage message, final SlackConstant slackConstant) { }

    public void sendSignUpMessage(final SlackSignUpMessage message, final SlackConstant slackConstant) { }
}
