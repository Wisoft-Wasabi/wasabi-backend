package io.wisoft.wasabi.domain.auth.web;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.customization.SignupRequestCustomization;
import io.wisoft.wasabi.domain.auth.application.AuthService;
import io.wisoft.wasabi.domain.auth.web.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.web.dto.LoginResponse;
import io.wisoft.wasabi.domain.auth.web.dto.SignupRequest;
import io.wisoft.wasabi.domain.auth.web.dto.SignupResponse;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.Part;
import io.wisoft.wasabi.global.config.common.Const;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import io.wisoft.wasabi.global.config.web.interceptor.AdminInterceptor;
import io.wisoft.wasabi.global.config.web.resolver.AnyoneResolver;
import io.wisoft.wasabi.global.config.web.resolver.MemberIdResolver;
import io.wisoft.wasabi.global.config.web.response.ResponseAspect;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private MemberIdResolver memberIdResolver;

    @MockBean
    private AnyoneResolver anyoneResolver;

    @MockBean
    private AdminInterceptor adminInterceptor;

    @SpyBean
    private ResponseAspect responseAspect;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원 가입")
    class SignUp {

        @DisplayName("입력한 정보가 적합하여 회원 가입에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(SignupRequestCustomization.class)
        void signup_success(final SignupRequest request) throws Exception {

            //given
            final var response = new SignupResponse(1L);
            given(authService.signup(request)).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    post("/auth/signup")
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isCreated());
        }

        @Test
        @DisplayName("입력한 정보가 유효성 검사에 적합하지 않아, 회원가입에 실패한다.")
        void signup_fail_invalid_email_format() throws Exception {

            //given
            final var request = new SignupRequest(
                    "not-email-format",
                    "pass12",
                    "pass12",
                    "name12",
                    "phoneNumber",
                    "refer12",
                    Part.BACKEND,
                    "wisoft",
                    "motto"
            );

            //when
            final var result = mockMvc.perform(
                    post("/auth/signup")
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        @DisplayName("이메일과 비밀번호가 일치해, 로그인에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(NotSaveMemberCustomization.class)
        void login_success(final Member member) throws Exception {

            //given
            final var request = new LoginRequest(member.getEmail(), member.getPassword());

            final String accessToken = jwtTokenProvider.createAccessToken(1L, member.getName(), member.getRole(), false);

            final var response = new LoginResponse(
                    member.getName(),
                    member.getRole(),
                    member.isActivation(),
                    accessToken,
                    Const.TOKEN_TYPE
            );

            given(authService.login(request)).willReturn(response);

            //when
            final var result = mockMvc.perform(
                    post("/auth/login")
                            .accept(APPLICATION_JSON)
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isOk());
        }

    }

    @DisplayName("입력한 정보가 유효성 검사에 적합하지 않아, 로그인에 실패한다.")
    @ParameterizedTest
    @AutoSource
    void login_fail_invalid_email_format(final SignupRequest request) throws Exception {

        //given

        //when
        final var result = mockMvc.perform(
                post("/auth/login")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isBadRequest());
    }
}