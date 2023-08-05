package io.wisoft.wasabi.domain.auth;

import autoparams.AutoSource;
import autoparams.customization.Customization;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wisoft.wasabi.customization.NotSaveMemberCustomization;
import io.wisoft.wasabi.customization.SignupRequestCustomization;
import io.wisoft.wasabi.domain.auth.dto.request.LoginRequest;
import io.wisoft.wasabi.domain.auth.dto.request.SignupRequest;
import io.wisoft.wasabi.domain.auth.dto.response.LoginResponse;
import io.wisoft.wasabi.domain.auth.dto.response.SignupResponse;
import io.wisoft.wasabi.domain.member.Member;
import io.wisoft.wasabi.global.config.common.annotation.MemberIdResolver;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @Spy
    private ObjectMapper objectMapper;

    private final String TOKEN_TYPE = "bearer";

    @Nested
    @DisplayName("회원 가입")
    class SignUp {

        @DisplayName("입력 정보가 적합하여 회원 가입에 성공한다.")
        @ParameterizedTest
        @AutoSource
        @Customization(SignupRequestCustomization.class)
        void signup_success(final SignupRequest request) throws Exception {

            //given
            final var response = new SignupResponse(1L, request.name());
            given(authService.signup(request)).willReturn(response);

            //when
            final var result = mockMvc.perform(post("/auth/signup")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isCreated());
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

            final String accessToken = jwtTokenProvider.createAccessToken(1L, member.getName(), member.getRole());

            final LoginResponse loginResponse = new LoginResponse(
                    member.getName(),
                    member.getRole(),
                    member.isActivation(),
                    accessToken,
                    TOKEN_TYPE
            );

            given(authService.login(request)).willReturn(loginResponse);

            //when
            final var result = mockMvc.perform(post("/auth/login")
                    .accept(APPLICATION_JSON)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            //then
            result.andExpect(status().isOk());
        }
    }
}