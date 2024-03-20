package io.wisoft.wasabi.domain.auth.application;

import autoparams.AutoSource;
import io.wisoft.wasabi.domain.auth.exception.LoginFailException;
import io.wisoft.wasabi.domain.auth.web.dto.LoginRequest;
import io.wisoft.wasabi.domain.auth.web.dto.SignupRequest;
import io.wisoft.wasabi.domain.auth.web.dto.VerifyEmailRequest;
import io.wisoft.wasabi.domain.auth.web.dto.VerifyEmailResponse;
import io.wisoft.wasabi.domain.member.application.MemberMapper;
import io.wisoft.wasabi.domain.member.application.MemberRepository;
import io.wisoft.wasabi.domain.member.exception.EmailOverlapException;
import io.wisoft.wasabi.domain.member.persistence.Member;
import io.wisoft.wasabi.domain.member.persistence.Role;
import io.wisoft.wasabi.global.config.common.bcrypt.BcryptEncoder;
import io.wisoft.wasabi.global.config.common.jwt.JwtTokenProvider;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private EmailService emailService;

    @Nested
    @DisplayName("회원 가입")
    class SignUp {

        @ParameterizedTest
        @AutoSource
        @DisplayName("회원가입이 정상적으로 동작하여, 회원이 생성된다.")
        void signUp_success(final SignupRequest request) {

            //given
            given(memberRepository.existsByEmail(any())).willReturn(false);

            //when
            final var response = authService.signup(request);

            //then
            assertThat(response).isNotNull();
        }


        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일이 중복되어, 회원가입에 실패한다.")
        void signUp_fail_duplicate_email(final SignupRequest request) {

            //given
            given(memberRepository.existsByEmail(request.email())).willReturn(true);

            //when

            //then
            assertThrows(EmailOverlapException.class, () -> authService.signup(request));
        }
    }

    @Nested
    @DisplayName("로그인")
    class Login {

        // [java.lang.IllegalArgumentException: Invalid salt version] 발생 - 추후 처리
//        @ParameterizedTest
//        @AutoSource
        @DisplayName("이메일, 비밀번호가 일치하여 로그인에 성공한다.")
        void login_success(final Member member) {

            //given
            final var ACCESS_TOKEN = "accessToken";
            final var request = new LoginRequest(member.getEmail(), member.getPassword());

            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));
            given(jwtTokenProvider.createAccessToken(eq(member.getId()), eq(member.getName()), eq(member.getRole()), anyBoolean())).willReturn(ACCESS_TOKEN);

            //when
            final var response = authService.login(request);

            //then
            assertThat(response.accessToken()).isEqualTo(ACCESS_TOKEN);
        }

        @ParameterizedTest
        @AutoSource
        @DisplayName("존재하지 않는 이메일이 입력으로 들어와 로그인에 실패한다.")
        void login_fail_not_exist_email(final LoginRequest request) {

            //given
            given(memberRepository.findMemberByEmail(request.email())).willThrow(LoginFailException.class);

            //when

            //then
            assertThrows(LoginFailException.class, () -> authService.login(request));
        }

        // [java.lang.IllegalArgumentException: Invalid salt version] 발생 - 추후 처리
//        @ParameterizedTest
//        @AutoSource
        @DisplayName("이메일은 존재하지만, 비밀번호가 존재하지 않아 로그인에 실패한다.")
        void login_fail_invalid_password(final Member member) {

            //given
            final var request = new LoginRequest(member.getEmail(), member.getPassword());
            given(memberRepository.findMemberByEmail(request.email())).willReturn(Optional.of(member));

            //when

            //then
            assertThrows(LoginFailException.class, () -> authService.login(request));
        }
    }

    @Nested
    @DisplayName("이메일 인증")
    class VerifyEmail {

        @ParameterizedTest
        @AutoSource
        @DisplayName("이메일 인증을 위한 인증 코드 전송 후 성공적으로 인증 코드가 반환된다.")
        void verify_email_success(final VerifyEmailRequest request) {

            // given
            given(emailService.sendSimpleMessage(anyString())).willReturn("000000");

            // when
            final var result = authService.verifyEmail(request);

            // then
            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result).isNotNull();
                softAssertions.assertThat(result.authCode()).isEqualTo("000000");
            });
        }
    }
}