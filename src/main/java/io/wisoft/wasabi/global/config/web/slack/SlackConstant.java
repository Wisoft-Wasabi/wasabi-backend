package io.wisoft.wasabi.global.config.web.slack;

public enum SlackConstant {

    ERROR_CHANNEL("#backend-server-error");

    private String channel;

    SlackConstant(final String channel) {
        this.channel = channel;
    }

    public String getChannel() {
        return channel;
    }
}
