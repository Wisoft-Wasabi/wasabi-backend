package io.wisoft.wasabi.domain.auth;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.SignupRequestCustomization;
import io.wisoft.wasabi.domain.auth.web.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.web.dto.SignupRequest;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.persistence.Role;
import io.wisoft.wasabi.global.config.common.bcrypt.BcryptEncoder;
import io.wisoft.wasabi.setting.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthIntegrationTest extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Nested
    @DisplayName("회원 가입")
    class SignUp {

        @DisplayName("입력 정보가 적합하여 회원 가입에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(SignupRequestCustomization.class)
        void signup_success(final SignupRequest request) throws Exception {

            //given

            //when
            final var result = mockMvc.perform(post("/auth/signup")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isCreated())
                    .andExpect(jsonPath("$.data.id").exists());
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @DisplayName("이메일과 비밀번호가 일치해, 로그인에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(SignupRequestCustomization.class)
        void login_success(final SignupRequest request) throws Exception {

            //given
            final Member member = new Member(
                    request.email(),
                    BcryptEncoder.encrypt(request.password()),
                    request.name(),
                    request.phoneNumber(),
                    false,
                    Role.GENERAL,
                    request.referenceUrl(),
                    request.part(),
                    request.organization(),
                    request.motto()
            );

            memberRepository.save(member);

            final var loginRequest = new LoginRequest(request.email(), request.password());

            //when
            final var result = mockMvc.perform(post("/auth/login")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)));

            //then
            result.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
        }
    }
}
