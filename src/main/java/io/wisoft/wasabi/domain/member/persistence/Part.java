package io.wisoft.wasabi.domain.member.persistence;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.wisoft.wasabi.domain.member.application.PartDeserializer;
import org.springframework.util.StringUtils;

@JsonDeserialize(using = PartDeserializer.class)
public enum Part {
    BACKEND("Backend"), FRONTEND("Frontend"), MOBILE("Mobile"), INFRA("Infra"),
    DBA("Dba"), DEVELOPER("Developer"), UNDEFINED("Undefined");

    private final String value;

    Part(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @JsonCreator
    public static Part fromString(final String value) {

        if (!StringUtils.hasText(value)) {
            return UNDEFINED;
        }

        try {
            return Part.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return UNDEFINED;
        }
    }
}
