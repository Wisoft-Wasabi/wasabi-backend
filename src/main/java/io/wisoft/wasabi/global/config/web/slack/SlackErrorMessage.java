package io.wisoft.wasabi.global.config.web.slack;

import io.wisoft.wasabi.global.config.web.response.ResponseType;

import java.time.LocalDateTime;

public record SlackErrorMessage(LocalDateTime dateTime, ResponseType responseType) {

    @Override
    public String toString() {
        return """
            장애 발생! ❌
            상태 = %s
            에러 코드 = %s
            에러 발생 일시 = %s
            에러 메시지 = %s
            """.formatted(responseType.getStatus(),
                responseType.getCode(),
                dateTime.withNano(0),
                responseType.getMessage());
    }

}
