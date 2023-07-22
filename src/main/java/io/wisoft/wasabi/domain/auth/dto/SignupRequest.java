package io.wisoft.wasabi.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupRequest(
        @NotBlank(message = "Email을 입력해주세요.") @Email String email,
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.")
        @NotBlank(message = "password를 입력하세요.")
        String password,
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{4,20}",
                message = "비밀번호는 영문과 숫자가 포함된 4자 ~ 20자의 비밀번호여야 합니다.") @NotBlank(message = "password를 입력하세요.")

        String checkPassword,
        @NotBlank String name,
        @NotBlank String phoneNumber
) {
}
