package io.wisoft.wasabi.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.wisoft.wasabi.domain.member.Part;
import io.wisoft.wasabi.global.config.common.annotation.PasswordCheck;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.util.StringUtils;

@PasswordCheck(field = "password", fieldMatch = "checkPassword")
public record SignupRequest(
        @NotBlank(message = "이메일을 입력해주세요.") @Email String email,
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
        @NotBlank(message = "비밀번호를 입력하세요.")
        String password,
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.") @NotBlank(message = "비밀번호를 입력하세요.")
        String checkPassword,
        @NotBlank(message = "이름을 입력하세요.") String name,
        @NotBlank(message = "전화번호를 입력하세요.") String phoneNumber,

        @Nullable
        String referenceUrl,
        @Nullable
        Part part,
        @Nullable
        String organization,
        @Nullable
        String motto
) {
    @JsonCreator
    public SignupRequest(
            final String email,
            final String password,
            final String checkPassword,
            final String name,
            final String phoneNumber,
            final String referenceUrl,
            final Part part,
            final String organization,
            final String motto
    ) {
        this.email = email;
        this.password = password;
        this.checkPassword = checkPassword;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.referenceUrl = StringUtils.hasText(referenceUrl) ? referenceUrl : "www.wisoft.io";
        this.part = part == null ? Part.UNDEFINED : part;
        this.organization = StringUtils.hasText(organization) ? organization : "wisoft";
        this.motto = StringUtils.hasText(motto) ? motto : "아자아자";
    }
}
