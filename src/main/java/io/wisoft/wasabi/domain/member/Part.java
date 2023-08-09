package io.wisoft.wasabi.domain.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.util.StringUtils;

@JsonDeserialize(using = PartDeserializer.class)
public enum Part {
    BACKEND("Backend"), FRONTEND("Frontend"), UNDEFINED("Undefined");

    private final String value;

    Part(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator // 추가: JSON 파싱 시에 대체값으로 변환
    public static Part fromString(final String value) {
        if (!StringUtils.hasText(value)) {
            return UNDEFINED;
        }
        try {
            return Part.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNDEFINED; // 유효하지 않은 값이면 기본값인 UNDEFINED 반환
        }
    }
}
